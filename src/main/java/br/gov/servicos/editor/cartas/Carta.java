package br.gov.servicos.editor.cartas;

import br.gov.servicos.editor.servicos.Metadados;
import br.gov.servicos.editor.utils.LeitorDeArquivos;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;
import static org.eclipse.jgit.lib.Constants.R_HEADS;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class Carta {

    @Getter
    String id;

    RepositorioGit repositorio;
    private LeitorDeArquivos leitorDeArquivos;

    private Carta(String id, RepositorioGit repositorio, LeitorDeArquivos leitorDeArquivos) {
        this.id = id;
        this.repositorio = repositorio;
        this.leitorDeArquivos = leitorDeArquivos;
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

    @Component
    @FieldDefaults(level = PRIVATE, makeFinal = true)
    public static class Formatter implements org.springframework.format.Formatter<Carta> {
        RepositorioGit repositorio;
        LeitorDeArquivos leitorDeArquivos;

        @Autowired
        public Formatter(RepositorioGit repositorio, LeitorDeArquivos leitorDeArquivos) {
            this.repositorio = repositorio;
            this.leitorDeArquivos = leitorDeArquivos;
        }

        @Override
        public Carta parse(String text, Locale locale) {
            return new Carta(text, repositorio, leitorDeArquivos);
        }

        @Override
        public String print(Carta object, Locale locale) {
            return object.toString();
        }
    }

}
