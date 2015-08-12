package br.gov.servicos.editor.servicos;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class ExcluirCartaController {

    File repositorioCartasLocal;
    Cartas cartas;
    Slugify slugify;

    @Autowired
    ExcluirCartaController(File repositorioCartasLocal, Cartas cartas, Slugify slugify) {
        this.repositorioCartasLocal = repositorioCartasLocal;
        this.cartas = cartas;
        this.slugify = slugify;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/excluir/api/servico/{id}", method = DELETE)
    void excluirServico(
            @PathVariable("id") String id,
            @AuthenticationPrincipal User usuario
    ) throws IOException {
        cartas.comRepositorioAberto(git -> {
            cartas.pull(git);
            try {
                cartas.executaNoBranchDoServico(id, () -> {
                    cartas.commit(git,
                            "Serviço deletado",
                            usuario,
                            excluirCarta(git, id));

                    return null;
                });
                return null;
            } finally {
                cartas.push(git, id);
            }
        });

    }

    @SneakyThrows
    public Path excluirCarta(Git git, String id) {
        Path caminho = Paths.get(repositorioCartasLocal.getAbsolutePath(), "cartas-servico", "v3", "servicos", id + ".xml");
        if (!caminho.toFile().exists())
            return null;

        git.rm().addFilepattern(repositorioCartasLocal.toPath().relativize(caminho).toString()).call();
        log.debug("git rm {}", caminho);

        return caminho;
    }


    @ResponseBody
    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void naoEncontrado() {
    }

}
