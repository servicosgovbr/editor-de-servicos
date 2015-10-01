package br.gov.servicos.editor.conteudo;

import br.gov.servicos.editor.git.Metadados;
import br.gov.servicos.editor.git.RepositorioGit;
import br.gov.servicos.editor.git.Revisao;
import br.gov.servicos.editor.security.UserProfile;
import br.gov.servicos.editor.utils.EscritorDeArquivos;
import br.gov.servicos.editor.utils.LeitorDeArquivos;
import br.gov.servicos.editor.utils.ReformatadorXml;
import com.github.slugify.Slugify;
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
public abstract class ConteudoVersionado<T> {

    @Getter(PROTECTED)
    RepositorioGit repositorio;

    LeitorDeArquivos leitorDeArquivos;

    EscritorDeArquivos escritorDeArquivos;

    Slugify slugify;

    ReformatadorXml reformatadorXml;

    public ConteudoVersionado(RepositorioGit repositorio, LeitorDeArquivos leitorDeArquivos, EscritorDeArquivos escritorDeArquivos, Slugify slugify, ReformatadorXml reformatadorXml) {
        this.repositorio = repositorio;
        this.leitorDeArquivos = leitorDeArquivos;
        this.escritorDeArquivos = escritorDeArquivos;
        this.slugify = slugify;
        this.reformatadorXml = reformatadorXml;
    }

    public abstract String getId();

    public abstract Path getCaminho();

    public abstract T getConteudo();

    public String getBranchRef() {
        return R_HEADS + getId();
    }

    private String getBranchMasterRef() {
        return R_HEADS + MASTER;
    }

    public Path getCaminhoAbsoluto() {
        return repositorio.getCaminhoAbsoluto().resolve(getCaminho()).toAbsolutePath();
    }

    public Path getCaminhoRelativo() {
        return repositorio.getCaminhoAbsoluto().relativize(getCaminhoAbsoluto());
    }

    protected Optional<Revisao> getRevisaoMaisRecenteDoArquivo() {
        return repositorio.getRevisaoMaisRecenteDoArquivo(getCaminhoRelativo());
    }

    protected Optional<Revisao> getRevisaoMaisRecenteDoBranch() {
        return repositorio.getRevisaoMaisRecenteDoBranch(getBranchRef());
    }

    @Cacheable
    public Metadados<T> getMetadados() {
        return new Metadados<T>()
                .withId(getId())
                .withPublicado(getRevisaoMaisRecenteDoArquivo().orElse(null))
                .withEditado(getRevisaoMaisRecenteDoBranch().orElse(null))
                .withConteudo(getConteudo());
    }

    @CacheEvict
    public void salvar(UserProfile profile, String conteudo) {
        repositorio.comRepositorioAbertoNoBranch(getBranchRef(), () -> {
            salvarConteudo(profile, getBranchRef(), conteudo);
            return null;
        });
    }

    @CacheEvict
    public void remover(UserProfile profile) {
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

    private boolean isPublicado() {
        return Files.exists(getCaminhoAbsoluto());
    }

    @CacheEvict
    public void publicar() {
        repositorio.comRepositorioAbertoNoBranch(getBranchMasterRef(), () -> {
            repositorio.pull();

            repositorio.merge(getBranchRef());
            repositorio.push(getBranchMasterRef());

            return null;
        });
    }

    @CacheEvict
    public void renomear(UserProfile profile, String novoNome) {
        String novoId = slugify.slugify(novoNome);

        repositorio.comRepositorioAbertoNoBranch(getBranchRef(), uncheckedSupplier(() -> {
            repositorio.pull();
            alterarConteudo(profile, novoNome, getBranchRef());
            if (!getId().equals(novoId)) {
                repositorio.moveBranchPara(novoId);
                renomearConteudo(profile, novoId, novoId);
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
    }

    @CacheEvict
    public String getConteudoRaw() throws FileNotFoundException {
        return repositorio.comRepositorioAbertoNoBranch(getBranchRef(), () -> {
            repositorio.pull();
            return leitorDeArquivos.ler(getCaminhoAbsoluto().toFile());
        }).orElseThrow(
                () -> new FileNotFoundException("Não foi possível encontrar o serviço referente ao arquivo '" + getId() + "'")
        );
    }

    @SneakyThrows
    private String mudarNomeConteudo(String novoNome) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(getCaminhoAbsoluto().toFile());
        doc.getElementsByTagName("nome").item(0).setTextContent(novoNome);
        return reformatadorXml.formata(new DOMSource(doc));
    }


    private void salvarConteudo(UserProfile profile, String branch, String conteudo) {
        String mensagem = format("%s '%s'", getCaminhoAbsoluto().toFile().exists() ? "Altera" : "Cria", getId());

        repositorio.pull();
        escritorDeArquivos.escrever(getCaminhoAbsoluto(), conteudo);

        repositorio.add(getCaminhoRelativo());
        repositorio.commit(getCaminhoRelativo(), mensagem, profile);
        repositorio.push(branch);
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
