package br.gov.servicos.editor.editar;

import br.gov.servicos.editor.frontend.ExportadorServicoV2;
import br.gov.servicos.editor.servicos.Cartas;
import br.gov.servicos.editor.servicos.Servico;
import com.github.slugify.Slugify;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Optional;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequestMapping(value = "/editar/servico", method = POST)
public class EditarController {

    ExportadorServicoV2 exportadorV2;
    Cartas cartas;
    Slugify slugify;

    @Autowired
    public EditarController(ExportadorServicoV2 exportadorV2, Cartas cartas, Slugify slugify) {
        this.exportadorV2 = exportadorV2;
        this.cartas = cartas;
        this.slugify = slugify;
    }

    @RequestMapping
    RedirectView salvar(@ModelAttribute("servico") Servico servico,
                        @AuthenticationPrincipal User usuario) throws IOException {
        String id = servicoId(servico);
        cartas.salvarServicoV2(id, exportadorV2.exportar(servico), usuario);
        return new RedirectView(format("/editar/servico/%s", id));
    }

    @RequestMapping(params = {"add=solicitante"})
    ModelAndView adicionarSolicitante(Servico servico) {
        servico.getSolicitantes().add("");

        return new ModelAndView("index", "servico", servico);
    }

    @RequestMapping(params = {"add=legislacao"})
    ModelAndView adicionarLegislacao(Servico servico) {
        servico.getLegislacoes().add("");

        return new ModelAndView("index", "servico", servico);
    }

    private String servicoId(Servico servico) {
        return Optional.ofNullable(servico.getNome())
                .map(slugify::slugify)
                .filter(n -> !n.trim().isEmpty())
                .orElse("novo");
    }

}
