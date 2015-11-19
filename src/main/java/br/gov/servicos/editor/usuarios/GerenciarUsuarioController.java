package br.gov.servicos.editor.usuarios;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@Controller
public class GerenciarUsuarioController {

    @Autowired
    private UsuarioFactory factory;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PapelRepository papelRepository;

    @ModelAttribute("papeis")
    public Iterable<Papel> papularPapeis() {
        return this.papelRepository.findAll();
    }

    @RequestMapping(value = "/editar/usuarios/usuario", method = POST)
    ModelAndView criar(@Valid FormularioUsuario formularioUsuario, BindingResult result) {
        ModelMap model = new ModelMap();
        if (!result.hasErrors()) {
            usuarioRepository.save(factory.criarUsuario(formularioUsuario));
            model.addAttribute("formularioUsuario", new FormularioUsuario());
        } else {
            model.addAttribute("formularioUsuario", formularioUsuario);
        }
        return new ModelAndView("cadastrar", model);
    }

    @RequestMapping("/editar/usuarios/usuario")
    ModelAndView login(FormularioUsuario formularioUsuario) {
        ModelMap model = new ModelMap();
        model.addAttribute("formularioUsuario", formularioUsuario);
        return new ModelAndView("cadastrar", model);
    }

}
