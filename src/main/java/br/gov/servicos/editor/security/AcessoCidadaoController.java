package br.gov.servicos.editor.security;

import br.gov.servicos.editor.usuarios.FormularioAcessoCidadao;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/editar/acesso-cidadao")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AcessoCidadaoController {

    AcessoCidadaoService acessoCidadaoService;

    @Autowired
    public AcessoCidadaoController(AcessoCidadaoService acessoCidadaoService) {
        this.acessoCidadaoService = acessoCidadaoService;
    }

    @RequestMapping(method = RequestMethod.GET)
    ModelAndView acessoCidadao() {
        ModelMap model = new ModelMap();
        model.addAttribute("formularioAcessoCidadao", new FormularioAcessoCidadao());
        return new ModelAndView("acesso-cidadao", model);
    }

    @RequestMapping(method = RequestMethod.POST)
    ModelAndView acessoCidadao(@Valid FormularioAcessoCidadao formularioAcessoCidadao, BindingResult result) {
        if (!result.hasErrors()) {
            acessoCidadaoService.autenticaCidadao(formularioAcessoCidadao);
            return new ModelAndView("redirect:/editar");
        } else {
            ModelMap model = new ModelMap();
            model.addAttribute("formularioAcessoCidadao", formularioAcessoCidadao);
            return new ModelAndView("acesso-cidadao", model);
        }
    }

}
