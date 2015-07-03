package br.gov.servicos.editor.editar;

import br.gov.servicos.editor.servicos.CanalDePrestacao;
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
public class EditarCanaisDePrestacaoController {

    @RequestMapping(params = {"adicionarCanalDePrestacao"})
    ModelAndView adicionarCanalDePrestacao(Servico servico, @RequestParam("adicionarCanalDePrestacao") int indice) {
        servico.getEtapas().get(indice).getCanaisDePrestacao().add(new CanalDePrestacao());
        return new ModelAndView("index", "servico", servico);
    }

    @RequestMapping(params = {"removerCanalDePrestacao"})
    ModelAndView removerCanalDePrestacao(Servico servico, @RequestParam("removerCanalDePrestacao") String indStr) {
        IndiceCampoDeEtapa indice = IndiceCampoDeEtapa.from(indStr);
        servico.getEtapas()
                .get(indice.getIndiceEtapa())
                .getCanaisDePrestacao()
                .remove(indice.getIndiceCampo());
        return new ModelAndView("index", "servico", servico);
    }

}
