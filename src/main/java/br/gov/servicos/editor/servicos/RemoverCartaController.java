package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.cartas.Carta;
import br.gov.servicos.editor.cartas.RepositorioGit;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class RemoverCartaController {

    RepositorioGit repositorio;

    @Autowired
    RemoverCartaController(RepositorioGit repositorio) {
        this.repositorio = repositorio;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/editar/api/servico/{id}", method = DELETE)
    void remover(
            @PathVariable("id") Carta carta,
            @AuthenticationPrincipal User usuario
    ) throws IOException {
        repositorio.comRepositorioAbertoNoBranch(carta.getBranchRef(), () -> {
            repositorio.pull();

            try {
                repositorio.remove(carta.getCaminhoRelativo());
                repositorio.commit("Remove '" + carta.getId() + "'", usuario, carta.getCaminhoRelativo());
            } finally {
                repositorio.push(carta.getBranchRef());
            }

            return null;
        });
    }

    @ResponseBody
    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void naoEncontrado() {
    }

}
