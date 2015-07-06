package br.gov.servicos.editor.editar;

import br.gov.servicos.editor.servicos.Etapa;
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
public class EditarEtapasController {

    @RequestMapping(params = {"adicionarEtapa"})
    ModelAndView adicionarEtapa(Servico servico) {
        servico.getEtapas().add(new Etapa());
        return new ModelAndView("index", "servico", servico);
    }

    @RequestMapping(params = {"removerEtapa"})
    ModelAndView removerEtapa(Servico servico, @RequestParam("removerEtapa") int indice) {
        servico.getEtapas().remove(indice);
        return new ModelAndView("index", "servico", servico);
    }



}
