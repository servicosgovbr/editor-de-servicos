package br.gov.servicos.editor.servicos;

import com.github.slugify.Slugify;
import lombok.experimental.FieldDefaults;
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
            @PathVariable("id") String id,
            @AuthenticationPrincipal User usuario
    ) throws IOException {
        cartas.excluir(id, usuario);

    }

    @ResponseBody
    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void naoEncontrado() {
    }

}
