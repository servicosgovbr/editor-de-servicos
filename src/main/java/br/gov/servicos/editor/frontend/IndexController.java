package br.gov.servicos.editor.frontend;

import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import static lombok.AccessLevel.PRIVATE;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class IndexController {

    @RequestMapping("/")
    RedirectView root() {
        return new RedirectView("/editar/");
    }

    @RequestMapping({
            "/editar",
            "/editar/servico/**",
            "/editar/orgao/**",
            "/editar/pagina/**",
            "/editar/visualizar/**"})
    ModelAndView index() {
        return new ModelAndView("index");
    }

    @RequestMapping("/editar/ajuda-markdown")
    ModelAndView ajudaComMarkdown() {
        return new ModelAndView("ajuda-markdown");
    }
}
