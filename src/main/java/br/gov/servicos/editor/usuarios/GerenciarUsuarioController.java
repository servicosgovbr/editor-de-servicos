package br.gov.servicos.editor.usuarios;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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

    @RequestMapping(value = "/editar/usuarios/usuario", method = POST)
    RedirectView criar(FormularioUsuario formulario) {
        usuarioRepository.save(factory.criarUsuario(formulario));
        return new RedirectView("/editar");
    }

    @RequestMapping("/editar/usuarios/usuario")
    ModelAndView login() {
        ModelAndView cadastroView = new ModelAndView("cadastrar");
        cadastroView.addObject("papeis", papelRepository.findAll());
        return cadastroView;
    }

}
