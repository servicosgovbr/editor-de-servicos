package br.gov.servicos.editor.config;

import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

import static java.nio.file.Files.createTempDirectory;
import static lombok.AccessLevel.PRIVATE;

@Configuration
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RepositorioCartasConfig {

    File local;

    @SneakyThrows
    RepositorioCartasConfig() {
        local = createTempDirectory("editor-de-servicos").toFile();
        local.deleteOnExit();
    }

    @Bean
    public File repositorioCartasLocal() {
        return local;
    }

}
