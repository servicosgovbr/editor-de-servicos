package br.gov.servicos.editor.fixtures;

import br.gov.servicos.editor.fixtures.given.EstruturaRepositorio;
import br.gov.servicos.editor.git.RepositorioConfig;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.junit.rules.TemporaryFolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static lombok.AccessLevel.PRIVATE;

@Configuration
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Profile("teste")
public class RepositorioConfigTeste extends ExternalResource {

    private TemporaryFolder origin = new TemporaryFolder();
    private TemporaryFolder whereToClone = new TemporaryFolder();

    @SneakyThrows
    public RepositorioConfigTeste() {
        origin.create();
        whereToClone.create();
        criaRepoComEstrutura();
    }

    @Override
    protected void after() {
        origin.delete();
        whereToClone.delete();
    }

    @SneakyThrows
    private void criaRepoComEstrutura() {
        EstruturaRepositorio.mkdirs(origin.getRoot().toPath());
        Git.init().setDirectory(origin.getRoot()).call();
    }

    @Bean
    public RepositorioConfig testConfig() {
        return new RepositorioConfig(origin.getRoot().getAbsolutePath(),
                true,
                true,
                whereToClone.getRoot());
    }
}