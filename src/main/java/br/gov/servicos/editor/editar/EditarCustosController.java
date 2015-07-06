package br.gov.servicos.editor.editar;

import br.gov.servicos.editor.servicos.Custo;
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
public class EditarCustosController {

    @RequestMapping(params = {"adicionarCusto"})
    ModelAndView adicionarCusto(Servico servico, @RequestParam("adicionarCusto") int indiceEtapa) {
        servico.getEtapas().get(indiceEtapa).getCustos().add(new Custo());

        return new ModelAndView("index", "servico", servico);
    }

    @RequestMapping(params = {"removerCusto"})
    ModelAndView removerCusto(Servico servico, @RequestParam("removerCusto") String indices) {
        IndiceCampoDeEtapa indiceEtapa = IndiceCampoDeEtapa.from(indices);

        servico.getEtapas().get(indiceEtapa.getEtapa()).getCustos().remove(indiceEtapa.getCampo());

        return new ModelAndView("index", "servico", servico);
    }

}
