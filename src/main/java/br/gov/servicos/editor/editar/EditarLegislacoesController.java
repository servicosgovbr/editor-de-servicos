package br.gov.servicos.editor.editar;

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
public class EditarLegislacoesController {

    SalvarController salvar;

    @Autowired
    public EditarLegislacoesController(SalvarController salvar) {
        this.salvar = salvar;
    }

    @RequestMapping(params = {"adicionarLegislacao"})
    RedirectView adicionarLegislacao(
            Servico servico,
            @AuthenticationPrincipal User usuario
    ) throws IOException {
        servico.getLegislacoes().add("");
        String url = salvar.salvar(servico, usuario).getUrl();
        return new RedirectView(url + "#legislacoes");
    }

    @RequestMapping(params = {"removerLegislacao"})
    RedirectView removerLegislacao(
            Servico servico,
            @RequestParam("removerLegislacao") int indice,
            @AuthenticationPrincipal User usuario
    ) throws IOException {
        servico.getLegislacoes().remove(indice);
        String url = salvar.salvar(servico, usuario).getUrl();
        return new RedirectView(url + "#legislacoes");
    }

}
