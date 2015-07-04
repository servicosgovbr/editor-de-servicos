package br.gov.servicos.editor.editar;

import br.gov.servicos.editor.servicos.Servico;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequestMapping(value = "/editar/servico", method = POST)
public class EditarDocumentosController {

    @RequestMapping(params = {"adicionarDocumento"})
    ModelAndView adicionarDocumento(Servico servico, @RequestParam("adicionarDocumento") int indiceEtapa) {
        servico.getEtapas().get(indiceEtapa).getDocumentos().add("");

        return new ModelAndView("index", "servico", servico);
    }

    @RequestMapping(params = {"removerDocumento"})
    ModelAndView removerDocumento(Servico servico, @RequestParam("removerDocumento") String indStr) {
        IndiceCampoDeEtapa indice = IndiceCampoDeEtapa.from(indStr);
        servico.getEtapas()
                .get(indice.getEtapa())
                .getDocumentos()
                .remove(indice.getCampo());
        return new ModelAndView("index", "servico", servico);
    }

}
