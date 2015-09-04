package br.gov.servicos.editor.cartas;

import br.gov.servicos.editor.utils.LogstashProgressMonitor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
class Importador {

    String repositorioCartas;
    File repositorioCartasLocal;

    @NonFinal
    @Getter
    boolean importadoComSucesso;

    @Autowired
    Importador(@Value("${eds.cartas.repositorio}") String urlRepositorioCartas,
               File repositorioCartasLocal) {

        this.repositorioCartas = urlRepositorioCartas;
        this.repositorioCartasLocal = repositorioCartasLocal;
    }

    @PostConstruct
    @SneakyThrows
    void importaRepositorioDeCartas() {
        log.info("Importando repositório de cartas {} para {}", repositorioCartas, repositorioCartasLocal);
        Git.cloneRepository()
                .setURI(repositorioCartas)
                .setDirectory(repositorioCartasLocal)
                .setProgressMonitor(new LogstashProgressMonitor(log))
                .call();
        importadoComSucesso = true;
    }

}
