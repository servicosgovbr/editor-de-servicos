package br.gov.servicos.editor.conteudo;

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
    boolean deveImportar;

    @NonFinal
    @Getter
    boolean importadoComSucesso;

    @Autowired
    Importador(
            @Value("${eds.cartas.repositorio}") String urlRepositorioCartas,
            @Value("${flags.importar}") boolean deveImportar,
            File repositorioCartasLocal
    ) {
        this.repositorioCartas = urlRepositorioCartas;
        this.deveImportar = deveImportar;
        this.repositorioCartasLocal = repositorioCartasLocal;
    }

    @PostConstruct
    @SneakyThrows
    void importaRepositorioDeCartas() {
        if(!deveImportar) {
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
