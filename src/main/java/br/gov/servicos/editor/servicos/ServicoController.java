package br.gov.servicos.editor.servicos;

import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class ServicoController {

    ImportadorServicoV1 importadorV1;
    ImportadorServicoV2 importadorV2;

    @Autowired
    ServicoController(ImportadorServicoV1 importadorV1, ImportadorServicoV2 importadorV2) {
        this.importadorV1 = importadorV1;
        this.importadorV2 = importadorV2;
    }

    @RequestMapping(value = "/servico/{id}", method = GET)
    ModelAndView editar(@PathVariable("id") String id) throws IOException {
        return new ModelAndView("index", "servico",
                importadorV2.carregar(id)
                        .orElseGet(() -> importadorV1.carregar(id)
                        .orElseThrow(RuntimeException::new)));
    }

}
