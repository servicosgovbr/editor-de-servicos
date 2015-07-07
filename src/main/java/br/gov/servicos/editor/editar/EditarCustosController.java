package br.gov.servicos.editor.editar;

import br.gov.servicos.editor.servicos.Custo;
import br.gov.servicos.editor.servicos.Servico;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequestMapping(value = "/editar/servico", method = POST)
public class EditarCustosController {

    SalvarController salvar;

    @Autowired
    public EditarCustosController(SalvarController salvar) {
        this.salvar = salvar;
    }

    @RequestMapping(params = {"adicionarCusto"})
    RedirectView adicionarCusto(
            Servico servico,
            @RequestParam("adicionarCusto") int indiceEtapa,
            @AuthenticationPrincipal User usuario
    ) throws IOException {
        servico.getEtapas().get(indiceEtapa).getCustos().add(new Custo());
        return salvar.salvar(servico, usuario);
    }

    @RequestMapping(params = {"removerCusto"})
    RedirectView removerCusto(
            Servico servico,
            @RequestParam("removerCusto") String indices,
            @AuthenticationPrincipal User usuario
    ) throws IOException {
        IndiceCampoDeEtapa indiceEtapa = IndiceCampoDeEtapa.from(indices);
        servico.getEtapas().get(indiceEtapa.getEtapa()).getCustos().remove(indiceEtapa.getCampo());
        return salvar.salvar(servico, usuario);
    }

}
