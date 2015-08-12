package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.cartas.Carta;
import com.github.slugify.Slugify;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class ExcluirCartaController {

    Cartas cartas;
    Slugify slugify;

    @Autowired
    ExcluirCartaController(Cartas cartas, Slugify slugify) {
        this.cartas = cartas;
        this.slugify = slugify;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/excluir/api/servico/{id}", method = DELETE)
    void excluirServico(
            @PathVariable("id") Carta carta,
            @AuthenticationPrincipal User usuario
    ) throws IOException {
        cartas.comRepositorioAberto(git -> {
            cartas.pull(git);
            try {
                cartas.executaNoBranchDoServico(carta, () -> {
                    cartas.commit(git,
                            "Serviço deletado",
                            usuario,
                            excluirCarta(git, carta));

                    return null;
                });
                return null;
            } finally {
                cartas.push(git, carta);
            }
        });

    }

    @SneakyThrows
    public Path excluirCarta(Git git, Carta carta) {
        Path caminho = carta.caminhoAbsoluto();

        if (!caminho.toFile().exists()) {
            return null;
        }

        git.rm().addFilepattern(carta.caminhoRelativo()).call();
        log.debug("git rm {}", caminho);

        return caminho;
    }


    @ResponseBody
    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void naoEncontrado() {
    }

}
