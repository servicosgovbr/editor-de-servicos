package br.gov.servicos.editor.usuarios;

import br.com.caelum.stella.format.CPFFormatter;
import br.gov.servicos.editor.frontend.Siorg;
import br.gov.servicos.editor.security.TipoPermissao;
import br.gov.servicos.editor.security.UserProfiles;
import br.gov.servicos.editor.usuarios.cadastro.FormularioUsuario;
import br.gov.servicos.editor.usuarios.recuperarsenha.RecuperacaoSenhaService;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static br.gov.servicos.editor.security.TipoPermissao.CADASTRAR_OUTROS_ORGAOS;
import static lombok.AccessLevel.PRIVATE;
import static net.logstash.logback.marker.Markers.append;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GerenciarUsuarioController {

    public static final String COMPLETAR_CADASTRO = "completarCadastro";
    public static final String RECUPERAR_SENHA = "recuperarSenha";

    UsuarioFactory factory;

    UsuarioService usuarioService;

    PapelRepository papeis;

    RecuperacaoSenhaService tokenService;

    Siorg siorg;

    UserProfiles userProfiles;

    CPFFormatter cpfFormatter = new CPFFormatter();

    @Autowired
    public GerenciarUsuarioController(UsuarioFactory factory, UsuarioService usuarioService, PapelRepository papeis, RecuperacaoSenhaService tokenService, Siorg siorg, UserProfiles userProfiles) {
        this.factory = factory;
        this.usuarioService = usuarioService;
        this.papeis = papeis;
        this.tokenService = tokenService;
        this.siorg = siorg;
        this.userProfiles = userProfiles;
    }

    @ModelAttribute("papeis")
    public Iterable<Papel> popularPapeis() {
        Iterable<Papel> todosPapeis = papeis.findAll();
        return StreamSupport.stream(todosPapeis.spliterator(), false)
                .filter(papel -> userProfiles.temPermissao(TipoPermissao.CADASTRAR.comPapel(papel.getNome())))
                .collect(Collectors.toList());
    }

    @ModelAttribute("userProfiles")
    public UserProfiles getUserProfiles() {
        return userProfiles;
    }

    @ModelAttribute("siorg")
    public Siorg getSiorg() {
        return siorg;
    }

    @ModelAttribute("temPermissaoDeGerenciarUsuariosDeOutrosOrgaos")
    public boolean temPermissaoDeGerenciarUsuariosDeOutrosOrgaos() {
        return userProfiles.temPermissao(CADASTRAR_OUTROS_ORGAOS.getNome());

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
        if (verificarPermissao(formularioUsuario)) {
            throw new AccessDeniedException("Usuário sem permissão");
        }

        if (!result.hasErrors()) {
            Usuario usuarioSalvo = usuarioService.save(factory.criarUsuario(formularioUsuario));
            logUsuario("Usuario criado", usuarioSalvo);
            return intrucoesParaRecuperarSenha(usuarioSalvo, COMPLETAR_CADASTRO);
        } else {
            ModelMap model = new ModelMap();
            model.addAttribute("metodo", POST);
            return new ModelAndView("cadastrar", model);
        }
    }

    @RequestMapping(value = "/editar/usuarios/usuario", method = PUT)
    public ModelAndView atualizar(@Valid FormularioUsuario formularioUsuario, BindingResult result) {
        if (verificarPermissao(formularioUsuario)) {
            throw new AccessDeniedException("Usuário sem permissão");
        }
        if (!result.hasErrors()) {
            // TODO verificar se existe mais de um usuário com o mesmo cpf e lançar erro
            Usuario usuario = usuarioService.findByCpf(cpfFormatter.unformat(formularioUsuario.getCpf()));
            if (usuario != null) {
                usuario = factory.atualizaUsuario(usuario, formularioUsuario);
            } else {
                throw new UsuarioInexistenteException();
            }

            Usuario usuarioSalvo = usuarioService.save(usuario);
            logUsuario("Usuario atualizado", usuarioSalvo);
            return usuarios();

        } else {
            ModelMap model = new ModelMap();
            model.addAttribute("metodo", PUT);
            return new ModelAndView("cadastrar", model);
        }
    }

    @RequestMapping("/editar/usuarios/usuario")
    public ModelAndView login(FormularioUsuario formularioUsuario) {
        ModelMap model = new ModelMap();
        model.addAttribute("formularioUsuario", formularioUsuario.withEhInclusaoDeUsuario(Boolean.TRUE));
        model.addAttribute("metodo", POST);
        return new ModelAndView("cadastrar", model);
    }

    @RequestMapping(value = "/editar/usuarios/usuario/{usuarioId}/habilitar-desabilitar", method = POST)
    public String habilitarDesabilitarUsuario(@PathVariable("usuarioId") String usuarioId) {
        Usuario usuario = usuarioService.habilitarDesabilitarUsuario(usuarioId);
        logUsuario("Mudança de propriedade habilitado", usuario);
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
        FormularioUsuario formularioUsuario = factory.criaFormulario(usuario);

        ModelMap model = new ModelMap();
        model.addAttribute("formularioUsuario", formularioUsuario.withEhInclusaoDeUsuario(Boolean.FALSE));
        model.addAttribute("metodo", PUT);

        return new ModelAndView("cadastrar", model);
    }

    private boolean verificarPermissao(@Valid FormularioUsuario formularioUsuario) {
        Papel papel = papeis.findById(Long.valueOf(formularioUsuario.getPapelId()));
        return papel != null && !userProfiles.temPermissaoGerenciarUsuarioOrgaoEPapel(formularioUsuario.getSiorg(), papel.getNome());
    }

    private ModelAndView intrucoesParaRecuperarSenha(Usuario usuario, String pagina) {
        ModelMap model = new ModelMap();

        model.addAttribute("usuario", usuario);
        model.addAttribute("link", gerarLinkParaRecuperacaoDeSenha(usuario.getId().toString(), pagina));
        model.addAttribute("pagina", pagina);

        logUsuario("Token gerado para trocar senha", usuario);

        return new ModelAndView("instrucoes-recuperar-senha", model);
    }

    private String gerarLinkParaRecuperacaoDeSenha(String usuarioId, String pagina) {
        String token = tokenService.gerarTokenParaUsuario(usuarioId);
        return "/editar/recuperar-senha?token=" + token + "&usuarioId=" + usuarioId + "&pagina=" + pagina;
    }

    private static void logUsuario(String mensagem, Usuario usuario) {
        Marker marker = append("usuario.id", usuario.getId())
                .and(append("usuario.cpf", usuario.getCpf()))
                .and(append("usuario.habilitado", usuario.isHabilitado()))
                .and(append("usuario.nome", usuario.getNome()))
                .and(append("usuario.papel", usuario.getPapel().getNome()))
                .and(append("usuario.siorg", usuario.getSiorg()));

        log.debug(marker, mensagem);
    }

}
