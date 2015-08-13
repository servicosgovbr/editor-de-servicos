package br.gov.servicos.editor.cartas;

import br.gov.servicos.editor.servicos.Metadados;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.transport.RefSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

import static java.nio.charset.Charset.defaultCharset;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.joining;
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

    public String getConteudo() throws FileNotFoundException {
        return repositorio.executaNoBranchDoServico(this, leitor())
                .orElseThrow(() -> new FileNotFoundException(
                        "Não foi possível encontrar o serviço referente ao arquivo '" + getId() + "'"
                ));
    }

    @SneakyThrows
    private Supplier<Optional<String>> leitor() {
        return () -> {
            File arquivo = getCaminhoAbsoluto().toFile();
            if (arquivo.exists()) {
                log.info("Arquivo {} encontrado", arquivo);
                return ler(arquivo);
            }

            log.info("Arquivo {} não encontrado", arquivo);
            return empty();
        };
    }

    @SneakyThrows
    private Optional<String> ler(File arquivo) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), defaultCharset()))) {
            return of(reader.lines().collect(joining("\n")));
        }
    }


}
