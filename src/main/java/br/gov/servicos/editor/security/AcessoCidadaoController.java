package br.gov.servicos.editor.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/editar/acesso-cidadao")
public class AcessoCidadaoController {

    AcessoCidadaoService acessoCidadaoService;

    @Autowired
    public AcessoCidadaoController(AcessoCidadaoService acessoCidadaoService) {
        this.acessoCidadaoService = acessoCidadaoService;
    }

    @RequestMapping(method = RequestMethod.GET)
    ModelAndView acessoCidadao() {
        return new ModelAndView("acesso-cidadao");
    }

    @RequestMapping(method = RequestMethod.POST)
    RedirectView acessoCidadao(@RequestParam String nome, @RequestParam String email, @RequestParam String cpf) {
        acessoCidadaoService.autenticaCidadao(nome,email,cpf);
        return new RedirectView("/editar", true, false);
    }
}
