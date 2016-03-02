package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.utils.LeitorDeArquivos;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class ImportarXMLCartaController {

    @ResponseBody
    @RequestMapping(value = "/editar/api/importar-xml", method = GET, produces = "application/xml")
    String editar(@RequestParam("url") String url) throws ImportacaoXmlException {
        try {
            return new LeitorDeArquivos().ler(new URI(url))
                    .get();
        } catch (Exception e) {
            throw new ImportacaoXmlException(url, e);
        }
    }

}
