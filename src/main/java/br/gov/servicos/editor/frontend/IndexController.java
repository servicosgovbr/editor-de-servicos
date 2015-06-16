package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.servicos.Servico;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
class IndexController {

    @RequestMapping("/")
    ModelAndView index() {
        return new ModelAndView("index", "servico", new Servico());
    }

    @RequestMapping(value = "/salvar", method = POST)
    RedirectView salvar(@ModelAttribute("servico") Servico servico, BindingResult result) {
        System.out.println("servico = " + servico);
        return new RedirectView("/");
    }

}
