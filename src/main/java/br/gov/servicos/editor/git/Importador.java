package br.gov.servicos.editor.git;

import br.gov.servicos.editor.utils.LogstashProgressMonitor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class Importador {

    String repositorioCartas;
    File repositorioCartasLocal;
    boolean deveImportar;

    @NonFinal
    @Getter
    boolean importadoComSucesso;

    @Autowired
    Importador(RepositorioConfig config) {
        repositorioCartas = config.urlRepositorioCartas;
        deveImportar = config.deveImportar;
        repositorioCartasLocal = config.localRepositorioDeCartas;
    }

    @PostConstruct
    @SneakyThrows
    public void importaRepositorioDeCartas() {
        if (!deveImportar) {
            log.info("Importação do repositório de cartas desabilitada (FLAGS_IMPORTAR=false)");
            return;
        }

        log.info("Importando repositório de cartas {} para {}", repositorioCartas, repositorioCartasLocal);
        Git.cloneRepository()
                .setURI(repositorioCartas)
                .setDirectory(repositorioCartasLocal)
                .setProgressMonitor(new LogstashProgressMonitor(log))
                .setCloneAllBranches(true)
                .call();

        importadoComSucesso = true;
    }

}
