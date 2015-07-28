package br.gov.servicos.editor.servicos;

import com.github.slugify.Slugify;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class ServicoController {

    Cartas cartas;
    Slugify slugify;

    @Autowired
    ServicoController(Cartas cartas, Slugify slugify) {
        this.cartas = cartas;
        this.slugify = slugify;
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/servico/v2/{id}", method = GET, produces = "application/xml")
    String editarV2(@PathVariable("id") String id) throws IOException {
        return cartas.conteudoServicoV2(slugify.slugify(id))
                .orElseThrow(() -> new FileNotFoundException(
                        "Não foi possível encontrar o serviço referente ao arquivo '" + id + "'"
                ));
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/servico/v3/{id}", method = GET, produces = "application/xml")
    String editarV3(@PathVariable("id") String id) throws IOException {
        return cartas.conteudoServicoV3(slugify.slugify(id))
                .orElseThrow(() -> new FileNotFoundException(
                        "Não foi possível encontrar o serviço referente ao arquivo '" + id + "'"
                ));
    }

    @ResponseBody
    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void naoEncontrado() {
    }

}
