package br.gov.servicos.editor.frontend;

import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class IndexController {

    @RequestMapping("/")
    RedirectView root() {
        return new RedirectView("/editar/", true, false);
    }

    @RequestMapping({
            "/editar",
            "/editar/erro",
            "/editar/servico/**",
            "/editar/pagina/**",
            "/editar/orgao/**",
            "/editar/pagina-tematica/**",
            "/editar/importar-xml/**",
            "/editar/visualizar/**"})
    ModelAndView index() {
        return new ModelAndView("index");
    }

    @RequestMapping("/editar/autenticar")
    ModelAndView login() {
        return new ModelAndView("login");
    }

    @RequestMapping("/editar/acesso-cidadao")
    ModelAndView acessoCidadao() {
        return new ModelAndView("acesso-cidadao");
    }

    @RequestMapping("/editar/ajuda-markdown")
    ModelAndView ajudaComMarkdown() {
        return new ModelAndView("ajuda-markdown");
    }

    @RequestMapping(value = "/editar/acessoNegado", method = {DELETE, PUT, POST, GET, PATCH})
    ModelAndView acessoNegado() {
        return new ModelAndView("acessoNegado");
    }
}
