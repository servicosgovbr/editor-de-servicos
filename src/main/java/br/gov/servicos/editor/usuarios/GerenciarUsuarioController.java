package br.gov.servicos.editor.usuarios;

import br.gov.servicos.editor.usuarios.cadastro.FormularioUsuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@Controller
public class GerenciarUsuarioController {

    @Autowired
    private UsuarioFactory factory;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PapelRepository papelRepository;

    @Autowired
    private TokenRecuperacaoSenhaService tokenService;

    @ModelAttribute("papeis")
    public Iterable<Papel> papularPapeis() {
        return this.papelRepository.findAll();
    }

    @RequestMapping(value = "/editar/usuarios")
    public ModelAndView usuarios() {
        Iterable<Usuario> usuarios = usuarioService.findAll();
        ModelMap model = new ModelMap();
        model.addAttribute("usuarios", usuarios);
        return new ModelAndView("usuarios", model);
    }

    @RequestMapping(value = "/editar/usuarios/usuario", method = POST)
    public String criar(@Valid FormularioUsuario formularioUsuario, BindingResult result) {
        if (!result.hasErrors()) {
            usuarioService.save(factory.criarUsuario(formularioUsuario));
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

    @RequestMapping(value = "/editar/usuarios/usuario/{usuarioId}/recuperar-senha", method = POST)
    public ModelAndView requisitarTrocaSenha(@PathVariable("usuarioId") String usuarioId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        ModelMap model = new ModelMap();
        model.addAttribute("usuario", usuario);
        model.addAttribute("link", gerarLinkParaRecuperacaoDeSenha(usuarioId));
        return new ModelAndView("instrucoes-recuperar-senha", model);
    }

    @RequestMapping(value = "/editar/recuperar-senha", method = GET)
    public ModelAndView recuperacaoSenha(FormularioRecuperarSenha formularioRecuperarSenha) {
        ModelMap model = new ModelMap();
        model.addAttribute("formularioRecuperarSenha", formularioRecuperarSenha);
        return new ModelAndView("recuperar-senha");
    }

    @RequestMapping(value = "/editar/recuperar-senha", method = POST)
    public String recuperarSenha(@Valid FormularioRecuperarSenha formularioRecuperarSenha, BindingResult result) {
        if (!result.hasErrors()) {
            return "redirect:/editar/autenticar?senhaAlterada";
        } else {
            return "recuperar-senha";
        }
    }

    private String gerarLinkParaRecuperacaoDeSenha(String usuarioId) {
        String token = tokenService.gerarParaUsuario(usuarioId);
        return "/editar/recuperar-senha?token=" + token + "&usuarioId=" + usuarioId;
    }

}
