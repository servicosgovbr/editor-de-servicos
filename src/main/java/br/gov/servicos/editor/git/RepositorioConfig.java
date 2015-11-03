package br.gov.servicos.editor.git;

import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

import static java.nio.file.Files.createTempDirectory;
import static lombok.AccessLevel.PUBLIC;

@Component
@FieldDefaults(level = PUBLIC, makeFinal = true)
@Profile("!teste")
public class RepositorioConfig {

    String urlRepositorioCartas;
    boolean deveImportar;
    boolean fazerPush;
    public final File localRepositorioDeCartas;

    @Autowired
    public RepositorioConfig(@Value("${eds.cartas.repositorio}") String urlRepositorioCartas,
                             @Value("${flags.importar}") boolean deveImportar,
                             @Value("${flags.git.push}") boolean fazerPush) throws IOException {
        this(urlRepositorioCartas, deveImportar, fazerPush, createTempDirectory("editor-de-servicos").toFile());
    }

    @SneakyThrows
    public RepositorioConfig(String urlRepositorioCartas,
                             boolean deveImportar,
                             boolean fazerPush,
                             File localRepositorioDeCartas) {

        this.urlRepositorioCartas = urlRepositorioCartas;
        this.deveImportar = deveImportar;
        this.fazerPush = fazerPush;
        this.localRepositorioDeCartas = localRepositorioDeCartas;
        this.localRepositorioDeCartas.deleteOnExit();
    }

}
