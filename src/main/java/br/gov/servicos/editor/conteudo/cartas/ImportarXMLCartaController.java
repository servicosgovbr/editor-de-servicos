package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.utils.LeitorDeArquivos;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class ImportarXMLCartaController {

    @ResponseBody
    @RequestMapping(value = "/editar/api/importar-xml", method = GET, produces = "application/xml")
    String editar(@RequestParam("url") String url) throws URISyntaxException, IOException {
        return new LeitorDeArquivos().ler(new URI(url))
                .get();
    }

}
