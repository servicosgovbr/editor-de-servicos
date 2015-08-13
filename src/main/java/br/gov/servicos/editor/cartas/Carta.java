package br.gov.servicos.editor.cartas;

import br.gov.servicos.editor.servicos.Metadados;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.transport.RefSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;
import static org.eclipse.jgit.lib.Constants.R_HEADS;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class Carta {

    @Configuration
    public static class Config {

        @Bean
        public Formatter<Carta> formatter(RepositorioGit repositorio) {
            return new Formatter<Carta>() {
                @Override
                public Carta parse(String text, Locale locale) {
                    return new Carta(text, repositorio);
                }

                @Override
                public String print(Carta object, Locale locale) {
                    return object.toString();
                }
            };
        }
    }

    @Getter
    String id;

    RepositorioGit repositorio;

    private Carta(String id, RepositorioGit repositorio) {
        this.id = id;
        this.repositorio = repositorio;
    }

    public Path getCaminhoAbsoluto() {
        return repositorio.getCaminhoAbsoluto().resolve(Paths.get("cartas-servico", "v3", "servicos", id + ".xml")).toAbsolutePath();
    }

    public RefSpec getRefSpec() {
        return new RefSpec(id + ":" + id);
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

}
