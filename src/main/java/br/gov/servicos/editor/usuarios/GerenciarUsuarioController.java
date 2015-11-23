package br.gov.servicos.editor.usuarios;

import br.gov.servicos.editor.usuarios.cadastro.FormularioUsuario;
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

    @RequestMapping(value = "/editar/usuarios")
    public ModelAndView usuarios() {
        Iterable<Usuario> usuarios = usuarioRepository.findAll();
        ModelMap model = new ModelMap();
        model.addAttribute("usuarios", usuarios);
        return new ModelAndView("usuarios", model);
    }

    @RequestMapping(value = "/editar/usuarios/usuario", method = POST)
    public String criar(@Valid FormularioUsuario formularioUsuario, BindingResult result) {
        if (!result.hasErrors()) {
            usuarioRepository.save(factory.criarUsuario(formularioUsuario));
            return "redirect:/editar/usuarios/usuario?sucesso";
        } else {
            return "cadastrar";
        }
    }

    @RequestMapping("/editar/usuarios/usuario")
    public ModelAndView login(FormularioUsuario formularioUsuario) {
        ModelMap model = new ModelMap();
        model.addAttribute("formularioUsuario", formularioUsuario);
        return new ModelAndView("cadastrar", model);
    }

}
