package br.gov.servicos.editor.servicos;

import com.github.slugify.Slugify;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @RequestMapping(value = "/editar/api/servico/{versao}/{id}", method = GET, produces = "application/xml")
    String editar(
            @PathVariable("versao") String versao,
            @PathVariable("id") String id
    ) throws IOException {
        return cartas.conteudoServicoV2(slugify.slugify(id))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Não foi possível encontrar o serviço referente ao arquivo '" + id + "'"
                ));
    }
}
