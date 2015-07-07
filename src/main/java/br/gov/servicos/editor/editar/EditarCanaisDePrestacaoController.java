package br.gov.servicos.editor.editar;

import br.gov.servicos.editor.servicos.CanalDePrestacao;
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
public class EditarCanaisDePrestacaoController {

    SalvarController salvar;

    @Autowired
    public EditarCanaisDePrestacaoController(SalvarController salvar) {
        this.salvar = salvar;
    }

    @RequestMapping(params = {"adicionarCanalDePrestacao"})
    RedirectView adicionarCanalDePrestacao(
            Servico servico,
            @RequestParam("adicionarCanalDePrestacao") int indice,
            @AuthenticationPrincipal User usuario
    ) throws IOException {
        servico.getEtapas().get(indice).getCanaisDePrestacao().add(new CanalDePrestacao());
        return salvar.salvar(servico, usuario);
    }

    @RequestMapping(params = {"removerCanalDePrestacao"})
    RedirectView removerCanalDePrestacao(
            Servico servico,
            @RequestParam("removerCanalDePrestacao") String index,
            @AuthenticationPrincipal User usuario
    ) throws IOException {
        IndiceCampoDeEtapa indice = IndiceCampoDeEtapa.from(index);
        servico.getEtapas()
                .get(indice.getEtapa())
                .getCanaisDePrestacao()
                .remove(indice.getCampo());

        return salvar.salvar(servico, usuario);
    }

}
