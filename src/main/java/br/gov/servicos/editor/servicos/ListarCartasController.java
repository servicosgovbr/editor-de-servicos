package br.gov.servicos.editor.servicos;

import com.github.slugify.Slugify;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class ListarCartasController {

    Cartas cartas;
    Slugify slugify;

    @Autowired
    ListarCartasController(Cartas cartas, Slugify slugify) {
        this.cartas = cartas;
        this.slugify = slugify;
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/servicos", method = GET)
    Iterable<Metadados> listar() throws IOException {
        return cartas.listar();
    }

}
