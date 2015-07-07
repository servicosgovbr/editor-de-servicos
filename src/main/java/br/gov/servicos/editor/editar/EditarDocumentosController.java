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
public class EditarDocumentosController {

    SalvarController salvar;

    @Autowired
    public EditarDocumentosController(SalvarController salvar) {
        this.salvar = salvar;
    }

    @RequestMapping(params = {"adicionarDocumento"})
    RedirectView adicionarDocumento(
            Servico servico,
            @RequestParam("adicionarDocumento") int indiceEtapa,
            @AuthenticationPrincipal User usuario
    ) throws IOException {
        servico.getEtapas().get(indiceEtapa).getDocumentos().add("");
        return salvar.salvar(servico, usuario);
    }

    @RequestMapping(params = {"removerDocumento"})
    RedirectView removerDocumento(
            Servico servico,
            @RequestParam("removerDocumento") String indStr,
            @AuthenticationPrincipal User usuario
    ) throws IOException {
        IndiceCampoDeEtapa indice = IndiceCampoDeEtapa.from(indStr);
        servico.getEtapas()
                .get(indice.getEtapa())
                .getDocumentos()
                .remove(indice.getCampo());
        return salvar.salvar(servico, usuario);
    }

}
