package br.gov.servicos.editor.cartas;

import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
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

    Boolean deveImportar;
    String repositorioCartas;
    File repositorioCartasLocal;

    @Autowired
    Importador(@Value("${flags.importar.cartas}") Boolean deveImportar,
               @Value("${eds.cartas.repositorio}") String urlRepositorioCartas,
               File repositorioCartasLocal) {

        this.deveImportar = deveImportar;
        this.repositorioCartas = urlRepositorioCartas;
        this.repositorioCartasLocal = repositorioCartasLocal;
    }

    @PostConstruct
    @SneakyThrows
    void importaRepositorioDeCartas() {
        if (!deveImportar) {
            log.info("Importação de cartas de serviço desligada (FLAGS_IMPORTAR_CARTAS=false).");
            return;
        }

        log.info("Importando repositório de cartas {} para {}", repositorioCartas, repositorioCartasLocal);
        Git.cloneRepository()
                .setURI(repositorioCartas)
                .setDirectory(repositorioCartasLocal)
                .call();
    }

}
