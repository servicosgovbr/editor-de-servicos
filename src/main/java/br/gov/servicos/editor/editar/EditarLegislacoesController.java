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
public class EditarLegislacoesController {

    @RequestMapping(params = {"adicionarLegislacao"})
    ModelAndView adicionarLegislacao(Servico servico) {
        servico.getLegislacoes().add("");

        return new ModelAndView("index", "servico", servico);
    }

    @RequestMapping(params = {"removerLegislacao"})
    ModelAndView removerLegislacao(Servico servico, @RequestParam("removerLegislacao") int indice) {
        servico.getLegislacoes().remove(indice);

        return new ModelAndView("index", "servico", servico);
    }

}
