package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.servicos.Cartas;
import br.gov.servicos.editor.servicos.Metadados;
import br.gov.servicos.editor.servicos.Servico;
import com.github.slugify.Slugify;
import lombok.experimental.FieldDefaults;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequestMapping("/editar")
class IndexController {

    ExportadorServicoV2 exportadorV2;
    Cartas cartas;
    Slugify slugify;

    @Autowired
    public IndexController(ExportadorServicoV2 exportadorV2, Cartas cartas, Slugify slugify) {
        this.exportadorV2 = exportadorV2;
        this.cartas = cartas;
        this.slugify = slugify;
    }

    @RequestMapping("/")
    ModelAndView index() {
        return new ModelAndView("index", "servico", new Servico()
                .withMetadados(new Metadados()
                        .withNovo(true)));
    }

    @RequestMapping(value = "/servico/novo", method = POST)
    RedirectView salvarNovo(@ModelAttribute("servico") Servico servico,
                            @AuthenticationPrincipal User usuario
    ) throws IOException, GitAPIException {
        return salvar(servico.getNome(), servico, usuario);
    }

    @RequestMapping(value = "/servico/{id}", method = POST)
    RedirectView salvar(@PathVariable("id") String unsafeId,
                        @ModelAttribute("servico") Servico servico,
                        @AuthenticationPrincipal User usuario
    ) throws IOException {
        String id = slugify.slugify(unsafeId);

        cartas.salvarServicoV2(id, exportadorV2.exportar(servico), usuario);

        return new RedirectView(format("/editar/servico/%s", id));
    }

}
