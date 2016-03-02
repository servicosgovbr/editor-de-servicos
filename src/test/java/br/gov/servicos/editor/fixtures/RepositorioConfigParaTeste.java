package br.gov.servicos.editor.fixtures;

import br.gov.servicos.editor.git.RepositorioConfig;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.nio.file.Files;
import java.nio.file.Path;

import static lombok.AccessLevel.PRIVATE;

@Configuration
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Profile("teste")
public class RepositorioConfigParaTeste {
    Path origin;

    @Getter
    Path localCloneRepositorio;

    @SneakyThrows
    public RepositorioConfigParaTeste() {
        origin = Files.createTempDirectory("editor-origin");
        localCloneRepositorio = Files.createTempDirectory("editor-clone");

        reset();
    }

    @SneakyThrows
    public void reset() {
        FileUtils.deleteDirectory(origin.toFile());
        FileUtils.deleteDirectory(localCloneRepositorio.toFile());

        Files.createDirectories(origin);
        Files.createDirectories(localCloneRepositorio);

        Git.init().setBare(true).setDirectory(origin.toFile()).call();

        origin.toFile().deleteOnExit();
        localCloneRepositorio.toFile().deleteOnExit();
    }

    @Bean
    public RepositorioConfig testConfig() {
        return new RepositorioConfig(origin.toString(),
                "",
                true,
                true,
                localCloneRepositorio.toFile());
    }

}