package br.gov.servicos.editor.fixtures;

import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.nio.file.Files;

import static lombok.AccessLevel.PRIVATE;

@Profile("teste")
@Configuration
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RepositorioConfigTeste {

    File local;

    @SneakyThrows
    RepositorioConfigTeste() {
        local = Files.createTempDirectory("editor-de-servicos-FUNCIONA").toFile();
        local.deleteOnExit();
    }

    @Bean
    public File repositorioCartasLocal() {
        return local;
    }

}