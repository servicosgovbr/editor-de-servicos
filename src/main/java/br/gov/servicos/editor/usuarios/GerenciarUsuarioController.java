package br.gov.servicos.editor.usuarios;

import br.com.caelum.stella.format.CPFFormatter;
import br.gov.servicos.editor.frontend.Siorg;
import br.gov.servicos.editor.usuarios.cadastro.FormularioUsuario;
import br.gov.servicos.editor.usuarios.recuperarsenha.CamposVerificacaoRecuperarSenha;
import br.gov.servicos.editor.usuarios.recuperarsenha.FormularioRecuperarSenha;
import br.gov.servicos.editor.usuarios.recuperarsenha.RecuperacaoSenhaService;
import br.gov.servicos.editor.usuarios.token.TokenExpirado;
import br.gov.servicos.editor.usuarios.token.TokenInvalido;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static net.logstash.logback.marker.Markers.append;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Slf4j
@Controller
public class GerenciarUsuarioController {

    public static final String COMPLETAR_CADASTRO = "completarCadastro";
    public static final String RECUPERAR_SENHA = "recuperarSenha";
    @Autowired
    private UsuarioFactory factory;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PapelRepository papelRepository;

    @Autowired
    private RecuperacaoSenhaService tokenService;

    @Autowired
    private Siorg siorg;

    private CPFFormatter cpfFormatter = new CPFFormatter();

    @ModelAttribute("papeis")
    public Iterable<Papel> papularPapeis() {
        return this.papelRepository.findAll();
    }

    @RequestMapping(value = "/editar/usuarios")
    public ModelAndView usuarios() {
        Iterable<Usuario> usuarios = usuarioService.findAll();
        ModelMap model = new ModelMap();
        model.addAttribute("siorg", siorg);
        model.addAttribute("usuarios", usuarios);
        return new ModelAndView("usuarios", model);
    }

    @RequestMapping(value = "/editar/usuarios/usuario", method = POST)
    public ModelAndView criar(@Valid FormularioUsuario formularioUsuario, BindingResult result) {
        if (!result.hasErrors()) {
            Usuario usuarioSalvo = usuarioService.save(factory.criarUsuario(formularioUsuario));
            usuarioLog("Usuario criado", usuarioSalvo);
            return intrucoesParaRecuperarSenha(usuarioSalvo, COMPLETAR_CADASTRO);
        } else {
            return new ModelAndView("cadastrar");
        }
    }

    @RequestMapping(value = "/editar/usuarios/usuario", method = PUT)
    public ModelAndView atualizar(@Valid FormularioUsuario formularioUsuario, BindingResult result) {
        if (!result.hasErrors()) {
            Usuario usuario = usuarioService.findByCpf(cpfFormatter.unformat(formularioUsuario.getCpf()));
            Usuario usuarioSalvo;
            if(usuario != null) {
                usuario = factory.atualizaUsuario(usuario, formularioUsuario);
            } else {
                throw new UsuarioInexistenteException();
            }
            usuarioSalvo = usuarioService.save(usuario);
            usuarioLog("Usuario criado", usuarioSalvo);
            return intrucoesParaRecuperarSenha(usuarioSalvo, COMPLETAR_CADASTRO);
        } else {
            return new ModelAndView("cadastrar");
        }
    }

    @RequestMapping("/editar/usuarios/usuario")
    public ModelAndView login(FormularioUsuario formularioUsuario) {
        ModelMap model = new ModelMap();
        model.addAttribute("formularioUsuario", formularioUsuario.withEhInclusaoDeUsuario(Boolean.TRUE));
        model.addAttribute("edicao", Boolean.FALSE);
        model.addAttribute("method", POST);
        return new ModelAndView("cadastrar", model);
    }

    @RequestMapping(value = "/editar/usuarios/usuario/{usuarioId}/habilitar-desabilitar", method = POST)
    public String habilitarDesabilitarUsuario(@PathVariable("usuarioId") String usuarioId) {
        Usuario usuario = usuarioService.habilitarDesabilitarUsuario(usuarioId);
        usuarioLog("Mudança de propriedade habilitado", usuario);
        return "redirect:/editar/usuarios";
    }

    @RequestMapping(value = "/editar/usuarios/usuario/{usuarioId}/recuperar-senha", method = POST)
    public ModelAndView requisitarTrocaSenha(@PathVariable("usuarioId") String usuarioId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        return intrucoesParaRecuperarSenha(usuario, RECUPERAR_SENHA);
    }

    @RequestMapping(value = "/editar/usuarios/usuario/{usuarioId}/editar", method = POST)
    public ModelAndView editarUsuario(@PathVariable("usuarioId") String usuarioId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        ModelMap model = new ModelMap();
        FormularioUsuario formularioUsuario = factory.criaFormulario(usuario);
        model.addAttribute("formularioUsuario", formularioUsuario);
        model.addAttribute("edicao", Boolean.TRUE);
        model.addAttribute("metodo", PUT);
        return new ModelAndView("cadastrar", model);
    }

    @RequestMapping(value = "/editar/recuperar-senha", method = GET)
    public ModelAndView recuperacaoSenha(@RequestParam(value = "pagina", defaultValue = RECUPERAR_SENHA) String pagina,
                                         FormularioRecuperarSenha formularioRecuperarSenha) {
        ModelMap model = new ModelMap();
        model.addAttribute("pagina", pagina);
        model.addAttribute("formularioRecuperarSenha", formularioRecuperarSenha);
        return new ModelAndView("recuperar-senha", model);
    }

    @RequestMapping(value = "/editar/recuperar-senha", method = POST)
    public ModelAndView recuperarSenha(@RequestParam(value = "pagina", defaultValue = "recuperarSenha") String pagina,
                                 @Valid FormularioRecuperarSenha formularioRecuperarSenha, BindingResult result) {
        if (!result.hasErrors()) {
            try {
                Usuario usuarioComSenhaNova = tokenService.trocarSenha(formularioRecuperarSenha);
                usuarioLog("Senha trocada", usuarioComSenhaNova);
                return new ModelAndView("redirect:/editar/autenticar?senhaAlterada");
            } catch(TokenInvalido e) {
                log.info("Falha na tentativa de trocar senha");
                result.addError(criarErroTokenInvalido(e));
                return retornarParaRecuperarSenha(pagina);
            }
        } else {
            return retornarParaRecuperarSenha(pagina);
        }
    }

    private ModelAndView retornarParaRecuperarSenha(
            @RequestParam(value = "pagina", defaultValue = RECUPERAR_SENHA) String pagina) {
        ModelMap model = new ModelMap();
        model.addAttribute("pagina", pagina);
        return new ModelAndView("recuperar-senha", model);
    }

    private ModelAndView intrucoesParaRecuperarSenha(Usuario usuario, String pagina) {
        ModelMap model = new ModelMap();
        model.addAttribute("usuario", usuario);
        model.addAttribute("link", gerarLinkParaRecuperacaoDeSenha(usuario.getId().toString(), pagina));
        model.addAttribute("pagina", pagina);
        usuarioLog("Token gerado para trocar senha", usuario);
        return new ModelAndView("instrucoes-recuperar-senha", model);
    }

    private FieldError criarErroTokenInvalido(TokenInvalido e) {
        String message;
        if(e instanceof TokenExpirado.CpfTokenInvalido) {
            TokenExpirado.CpfTokenInvalido cpfTokenInvalido = (TokenExpirado.CpfTokenInvalido) e;
            int tentativasSobrando = cpfTokenInvalido.getTentativasSobrando();
            message = criarMensagemTentativasSobrando(tentativasSobrando);
        } else {
            message = "Este link não é válido. Solicite um novo link para alterar sua senha.";
        }

        return new FieldError(FormularioRecuperarSenha.NOME_CAMPO, CamposVerificacaoRecuperarSenha.NOME,
                    message);
    }

    private String criarMensagemTentativasSobrando(int tentativasSobrando) {
        if(tentativasSobrando > 0) {
            return "O CPF informado não é compatível com o cadastrado. Você possui mais "
                    + tentativasSobrando + " tentativas.";
        } else {
            return  "O CPF informado não é compatível com o cadastrado e este link foi bloqueado. " +
                    "Entre em contato com o responsável pelo seu órgão para solicitar um novo link..";
        }
    }

    private String gerarLinkParaRecuperacaoDeSenha(String usuarioId, String pagina) {
        String token = tokenService.gerarTokenParaUsuario(usuarioId);
        return "/editar/recuperar-senha?token=" + token + "&usuarioId=" + usuarioId + "&pagina=" + pagina;
    }

    private void usuarioLog(String mensagem, Usuario usuario) {
        Marker marker = append("usuario.id", usuario.getId())
                .and(append("usuario.cpf", usuario.getCpf()))
                .and(append("usuario.habilitado", usuario.isHabilitado()))
                .and(append("usuario.nome", usuario.getNome()))
                .and(append("usuario.papel", usuario.getPapel().getNome()))
                .and(append("usuario.siorg", usuario.getSiorg()));

        log.debug(marker, mensagem);
    }

}
