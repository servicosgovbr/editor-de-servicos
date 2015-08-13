package br.gov.servicos.editor.cartas;

import br.gov.servicos.editor.servicos.Metadados;
import br.gov.servicos.editor.utils.EscritorDeArquivos;
import br.gov.servicos.editor.utils.LeitorDeArquivos;
import com.github.slugify.Slugify;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Optional;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;
import static org.eclipse.jgit.lib.Constants.R_HEADS;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class Carta {

    @Getter
    String id;

    RepositorioGit repositorio;
    LeitorDeArquivos leitorDeArquivos;
    EscritorDeArquivos escritorDeArquivos;

    private Carta(String id, RepositorioGit repositorio, LeitorDeArquivos leitorDeArquivos, EscritorDeArquivos escritorDeArquivos) {
        this.id = id;
        this.repositorio = repositorio;
        this.leitorDeArquivos = leitorDeArquivos;
        this.escritorDeArquivos = escritorDeArquivos;
    }

    public Path getCaminhoAbsoluto() {
        return repositorio.getCaminhoAbsoluto().resolve(Paths.get("cartas-servico", "v3", "servicos", id + ".xml")).toAbsolutePath();
    }

    public String getBranchRef() {
        return R_HEADS + id;
    }

    public Path getCaminhoRelativo() {
        return repositorio.getCaminhoAbsoluto().relativize(getCaminhoAbsoluto());
    }

    public Metadados getMetadados() {
        Optional<Metadados> commits = repositorio.getCommitMaisRecenteDoBranch(getBranchRef());

        if (commits.isPresent()) {
            return commits.get().withId(id);
        }

        commits = repositorio.getCommitMaisRecenteDoArquivo(getCaminhoRelativo());
        if (commits.isPresent()) {
            return commits.get().withId(id);
        }

        throw new RuntimeException("Não foi possível determinar a última revisão do arquivo");
    }

    public String getConteudo() throws FileNotFoundException {
        return repositorio.comRepositorioAbertoNoBranch(getBranchRef(),
                () -> leitorDeArquivos.ler(getCaminhoAbsoluto().toFile())
        ).orElseThrow(
                () -> new FileNotFoundException("Não foi possível encontrar o serviço referente ao arquivo '" + getId() + "'")
        );
    }

    public void salvar(User usuario, String conteudo) {
        repositorio.comRepositorioAbertoNoBranch(getBranchRef(), () -> {
            repositorio.pull();

            try {
                escritorDeArquivos.escrever(getCaminhoAbsoluto(), conteudo);

                repositorio.add(getCaminhoRelativo());

                String mensagem = format("%s '%s'", getCaminhoAbsoluto().toFile().exists() ? "Altera" : "Cria", getId());
                repositorio.commit(mensagem, usuario, getCaminhoRelativo());

            } finally {
                repositorio.push(getBranchRef());
            }

            return null;
        });
    }

    @Component
    @FieldDefaults(level = PRIVATE, makeFinal = true)
    public static class Factory implements org.springframework.format.Formatter<Carta> {
        Slugify slugify;
        RepositorioGit repositorio;
        LeitorDeArquivos leitorDeArquivos;
        EscritorDeArquivos escritorDeArquivos;

        @Autowired
        public Factory(Slugify slugify, RepositorioGit repositorio, LeitorDeArquivos leitorDeArquivos, EscritorDeArquivos escritorDeArquivos) {
            this.slugify = slugify;
            this.repositorio = repositorio;
            this.leitorDeArquivos = leitorDeArquivos;
            this.escritorDeArquivos = escritorDeArquivos;
        }

        @Override
        public Carta parse(String text, Locale locale) {
            return new Carta(slugify.slugify(text), repositorio, leitorDeArquivos, escritorDeArquivos);
        }

        @Override
        public String print(Carta object, Locale locale) {
            return object.getId();
        }
    }

}
