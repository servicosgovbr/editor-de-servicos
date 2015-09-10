package br.gov.servicos.editor.cartas;

import br.gov.servicos.editor.servicos.Metadados;
import br.gov.servicos.editor.servicos.Revisao;
import br.gov.servicos.editor.utils.EscritorDeArquivos;
import br.gov.servicos.editor.utils.LeitorDeArquivos;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.User;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Optional;

import static br.gov.servicos.editor.config.CacheConfig.METADADOS;
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

    public ConteudoVersionado(RepositorioGit repositorio, LeitorDeArquivos leitorDeArquivos, EscritorDeArquivos escritorDeArquivos) {
        this.repositorio = repositorio;
        this.leitorDeArquivos = leitorDeArquivos;
        this.escritorDeArquivos = escritorDeArquivos;
    }

    public abstract String getId();

    public abstract Path getCaminho();

    public abstract T getConteudo();

    protected String getBranchRef() {
        return R_HEADS + getId();
    }

    Path getCaminhoAbsoluto() {
        return repositorio.getCaminhoAbsoluto().resolve(getCaminho()).toAbsolutePath();
    }

    Path getCaminhoRelativo() {
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
    public void salvar(User usuario, String conteudo) {
        repositorio.comRepositorioAbertoNoBranch(getBranchRef(), () -> {
            repositorio.pull();

            try {
                escritorDeArquivos.escrever(getCaminhoAbsoluto(), conteudo);

                repositorio.add(getCaminhoRelativo());

                String mensagem = format("%s '%s'", getCaminhoAbsoluto().toFile().exists() ? "Altera" : "Cria", getId());
                repositorio.commit(getCaminhoRelativo(), mensagem, usuario);

            } finally {
                repositorio.push(getBranchRef());
            }

            return null;
        });
    }

    @CacheEvict
    public void remover(User usuario) {
        repositorio.comRepositorioAbertoNoBranch(this.getBranchRef(), () -> {
            repositorio.pull();

            try {
                repositorio.remove(getCaminhoRelativo());
                repositorio.commit(getCaminhoRelativo(), "Remove '" + getId() + "'", usuario);
            } finally {
                repositorio.push(getBranchRef());
            }

            return null;
        });
    }

    public void publicar(User usuario) {
        repositorio.comRepositorioAbertoNoBranch(R_HEADS + MASTER, () -> {
            repositorio.merge(getBranchRef());
            repositorio.push(R_HEADS + MASTER);
            return null;
        });

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
}
