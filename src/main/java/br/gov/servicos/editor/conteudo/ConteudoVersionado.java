package br.gov.servicos.editor.conteudo;

import br.gov.servicos.editor.frontend.Siorg;
import br.gov.servicos.editor.git.ConteudoMetadados;
import br.gov.servicos.editor.git.Metadados;
import br.gov.servicos.editor.git.RepositorioGit;
import br.gov.servicos.editor.git.Revisao;
import br.gov.servicos.editor.security.UserProfile;
import br.gov.servicos.editor.utils.EscritorDeArquivos;
import br.gov.servicos.editor.utils.LeitorDeArquivos;
import br.gov.servicos.editor.utils.ReformatadorXml;
import com.github.slugify.Slugify;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static br.gov.servicos.editor.config.CacheConfig.METADADOS;
import static br.gov.servicos.editor.utils.Unchecked.Supplier.uncheckedSupplier;
import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;
import static org.eclipse.jgit.lib.Constants.MASTER;
import static org.eclipse.jgit.lib.Constants.R_HEADS;

@FieldDefaults(level = PRIVATE, makeFinal = true)
@CacheConfig(cacheNames = METADADOS, keyGenerator = "geradorDeChavesParaCacheDeCommitsRecentes")
@AllArgsConstructor
public class ConteudoVersionado {

    @Getter
    String id;

    @Getter
    TipoPagina tipo;

    @Getter(PROTECTED)
    RepositorioGit repositorio;

    LeitorDeArquivos leitorDeArquivos;

    EscritorDeArquivos escritorDeArquivos;

    Slugify slugify;

    ReformatadorXml reformatadorXml;

    Siorg siorg;

    DeserializadorConteudoXML deserializadorConteudoXML;

    public Path getCaminho() {
        return Paths.get(tipo.getCaminhoPasta().toString(), getId() + "." + tipo.getExtensao());
    }

    public String getBranchRef() {
        return R_HEADS + tipo.prefixo() + getId();
    }

    public Path getCaminhoAbsoluto() {
        return repositorio.getCaminhoAbsoluto().resolve(getCaminho()).toAbsolutePath();
    }

    public Path getCaminhoRelativo() {
        return repositorio.getCaminhoAbsoluto().relativize(getCaminhoAbsoluto());
    }

    public boolean existe() {
        synchronized (RepositorioGit.class) {
            return existeNoMaster() || existeNoBranch();
        }
    }

    public boolean existeNoMaster() {
        synchronized (RepositorioGit.class) {
            return repositorio.comRepositorioAbertoNoBranch(getBranchMasterRef(),
                    () -> getCaminhoAbsoluto().toFile().exists());
        }
    }

    public boolean existeNoBranch() {
        synchronized (RepositorioGit.class) {
            return repositorio.existeBranch(getBranchRef())
                    && repositorio.comRepositorioAbertoNoBranch(getBranchRef(),
                    () -> getCaminhoAbsoluto().toFile().exists());
        }
    }

    @Cacheable
    public Metadados getMetadados() {
        synchronized (RepositorioGit.class) {
            return internalGetMetadados();
        }
    }

    @CacheEvict
    public void salvar(UserProfile profile, String conteudo) {
        synchronized (RepositorioGit.class) {
            repositorio.comRepositorioAbertoNoBranch(getBranchRef(), () -> {
                salvarConteudo(profile, getBranchRef(), conteudo);
                return null;
            });
        }
    }

    @CacheEvict
    public void remover(UserProfile profile) {
        synchronized (RepositorioGit.class) {
            repositorio.comRepositorioAbertoNoBranch(this.getBranchMasterRef(), () -> {
                repositorio.pull();

                repositorio.deleteLocalBranch(this.getBranchRef());
                repositorio.deleteRemoteBranch(this.getBranchRef());

                if (isPublicado()) {
                    repositorio.remove(getCaminhoRelativo());
                    repositorio.commit(getCaminhoRelativo(), "Remove '" + getId() + "'", profile);
                    repositorio.push(getBranchMasterRef());
                }
                return null;
            });
        }
    }

    @SneakyThrows
    @CacheEvict
    public void publicar(UserProfile profile) {
        synchronized (RepositorioGit.class) {
            if (!existeNoBranch()) {
                return;
            }

            String conteudo = getConteudoRaw();

            repositorio.comRepositorioAbertoNoBranch(getBranchMasterRef(), () -> {
                salvarConteudo(profile, getBranchMasterRef(), conteudo);

                repositorio.deleteLocalBranch(getBranchRef());
                repositorio.deleteRemoteBranch(getBranchRef());

                return null;
            });
        }
    }

    @CacheEvict
    public void descartarAlteracoes() {
        synchronized (RepositorioGit.class) {
            if (!existeNoBranch()) {
                return;
            }
            repositorio.comRepositorioAbertoNoBranch(getBranchMasterRef(), () -> {
                repositorio.deleteLocalBranch(getBranchRef());
                repositorio.deleteRemoteBranch(getBranchRef());
                return null;
            });
        }
    }

    @SneakyThrows
    @CacheEvict
    public void despublicarAlteracoes(UserProfile profile) {
        synchronized (RepositorioGit.class) {
            if (!existeNoMaster()) return;
            String conteudo = getConteudoRaw();
            salvar(profile, conteudo);
            despublicar(profile);
        }
    }

    @CacheEvict
    public String renomear(UserProfile profile, String novoNome) {
        synchronized (RepositorioGit.class) {
            String novoId = slugify.slugify(novoNome);
            String novoBranch = tipo.prefixo() + novoId;

            repositorio.comRepositorioAbertoNoBranch(getBranchRef(), uncheckedSupplier(() -> {
                repositorio.pull();
                alterarConteudo(profile, novoNome, getBranchRef());
                if (!getId().equals(novoId)) {
                    repositorio.moveBranchPara(novoBranch);
                    renomearConteudo(profile, novoId, novoBranch);
                    repositorio.deleteRemoteBranch(getBranchRef());
                }
                return null;
            }));

            repositorio.comRepositorioAbertoNoBranch(getBranchMasterRef(), uncheckedSupplier(() -> {
                repositorio.pull();
                if (isPublicado()) {
                    alterarConteudo(profile, novoNome, getBranchMasterRef());
                    if (!getId().equals(novoId)) {
                        renomearConteudo(profile, novoId, getBranchMasterRef());
                    }
                }
                return null;
            }));

            return novoId;
        }
    }

    @CacheEvict
    public String getConteudoRaw() throws FileNotFoundException {
        synchronized (RepositorioGit.class) {
            return repositorio.comRepositorioAbertoNoBranch(getBranchRef(), () -> {
                repositorio.pull();
                return leitorDeArquivos.ler(getCaminhoAbsoluto().toFile());
            }).orElseThrow(
                    () -> new FileNotFoundException("Não foi possível encontrar o " + getTipo().getNome() + " referente ao arquivo '" + getCaminhoAbsoluto() + "'")
            );
        }
    }

    public String getOrgaoId() {
        return getMetadados().getConteudo().getOrgaoId();
    }

    @SneakyThrows
    protected ConteudoMetadados getConteudoParaMetadados() {
        return deserializadorConteudoXML.deserializaConteudo(getConteudoRaw())
                .toConteudoMetadados(siorg);
    }

    protected Optional<Revisao> getRevisaoMaisRecenteDoMaster() {
        if (!existeNoMaster()) {
            return Optional.empty();
        }
        return repositorio.getRevisaoMaisRecenteDoBranch(getBranchMasterRef(), getCaminhoRelativo());
    }

    protected Optional<Revisao> getRevisaoMaisRecenteDoBranch() {
        if (!existeNoBranch()) {
            return Optional.empty();
        }
        return repositorio.getRevisaoMaisRecenteDoBranch(getBranchRef(), getCaminhoRelativo());
    }

    protected Metadados internalGetMetadados() {
        return new Metadados()
                .withId(getId())
                .withPublicado(getRevisaoMaisRecenteDoMaster().orElse(null))
                .withEditado(getRevisaoMaisRecenteDoBranch().orElse(null))
                .withConteudo(getConteudoParaMetadados());
    }

    private boolean isPublicado() {
        return Files.exists(getCaminhoAbsoluto());
    }

    private String getBranchMasterRef() {
        return R_HEADS + MASTER;
    }

    private void despublicar(UserProfile profile) {
        repositorio.comRepositorioAbertoNoBranch(getBranchMasterRef(), () -> {
            repositorio.remove(getCaminhoRelativo());
            repositorio.commit(getCaminhoRelativo(), "Despublica '" + getId() + "'", profile);
            repositorio.push(getBranchMasterRef());

            return null;
        });
    }

    @SneakyThrows
    private String mudarNomeConteudo(String novoNome) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(getCaminhoAbsoluto().toFile());
        doc.getElementsByTagName("nome").item(0).setTextContent(novoNome);
        return reformatadorXml.formata(new DOMSource(doc));
    }

    private void salvarConteudo(UserProfile profile, String branch, String conteudo, String mensagemBase) {
        String mensagem = format("%s '%s'", mensagemBase, getId());

        repositorio.pull();
        escritorDeArquivos.escrever(getCaminhoAbsoluto(), conteudo);

        repositorio.add(getCaminhoRelativo());
        repositorio.commit(getCaminhoRelativo(), mensagem, profile);
        repositorio.push(branch);
    }

    private void salvarConteudo(UserProfile profile, String branch, String conteudo) {
        salvarConteudo(profile, branch, conteudo, getCaminhoAbsoluto().toFile().exists() ? "Altera" : "Cria");
    }

    @SneakyThrows
    private void renomearConteudo(UserProfile profile, String nomeNovoArquivo, String branch) {
        String mensagem = format("Renomeia '%s' para '%s'", getId(), nomeNovoArquivo);
        Path novoCaminho = getCaminhoRelativo().resolveSibling(nomeNovoArquivo + ".xml");
        Files.move(getCaminhoAbsoluto(), getCaminhoAbsoluto().resolveSibling(nomeNovoArquivo + ".xml"));
        repositorio.remove(getCaminhoRelativo());
        repositorio.commit(getCaminhoRelativo(), mensagem, profile);
        repositorio.add(novoCaminho);
        repositorio.commit(novoCaminho, mensagem, profile);
        repositorio.push(branch);
    }

    private void alterarConteudo(UserProfile profile, String novoNome, String branch) {
        String conteudo = mudarNomeConteudo(novoNome);
        salvarConteudo(profile, branch, conteudo);
    }
}

