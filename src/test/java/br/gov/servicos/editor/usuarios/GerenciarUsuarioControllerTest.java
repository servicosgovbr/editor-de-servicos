package br.gov.servicos.editor.usuarios;

import br.gov.servicos.editor.security.UserProfiles;
import br.gov.servicos.editor.usuarios.cadastro.FormularioUsuario;
import br.gov.servicos.editor.usuarios.recuperarsenha.RecuperacaoSenhaService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import static br.gov.servicos.editor.usuarios.GerenciarUsuarioController.COMPLETAR_CADASTRO;
import static br.gov.servicos.editor.usuarios.GerenciarUsuarioController.RECUPERAR_SENHA;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GerenciarUsuarioControllerTest {

    public static final String CPF = "12312312319";
    private static final String TOKEN = "token";
    private static final String USUARIO_ID = "123412341234";
    private FormularioUsuario FORM_USUARIO = new FormularioUsuario().withPapelId("1");
    private Usuario USUARIO = new Usuario().withCpf(CPF).withId(Long.valueOf(USUARIO_ID)).withPapel(new Papel());

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private UsuarioFactory factory;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RecuperacaoSenhaService tokenService;

    @Mock
    private UserProfiles userProfiles;

    @Mock
    private PapelRepository papelRepository;

    @InjectMocks
    private GerenciarUsuarioController controller;

    @Before
    public void setUp() {
        when(papelRepository.findById(any())).thenReturn(new Papel());
    }

    @Test
    public void salvaNovoUsuario() {
        when(userProfiles.temPermissaoGerenciarUsuarioOrgaoEPapel(any(), any())).thenReturn(true);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(factory.criarUsuario(FORM_USUARIO)).thenReturn(USUARIO);
        when(usuarioService.save(USUARIO)).thenReturn(USUARIO);
        controller.criar(FORM_USUARIO, bindingResult);
        verify(usuarioService).save(USUARIO);
    }

    @Test
    public void vaiParaPaginaDeIntrucoesParaFinalizarCadastro() {
        when(userProfiles.temPermissaoGerenciarUsuarioOrgaoEPapel(any(), any())).thenReturn(true);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(factory.criarUsuario(FORM_USUARIO)).thenReturn(USUARIO);
        when(usuarioService.save(USUARIO)).thenReturn(USUARIO);
        when(tokenService.gerarTokenParaUsuario(USUARIO_ID)).thenReturn(TOKEN);
        ModelAndView modelAndView = controller.criar(FORM_USUARIO, bindingResult);
        assertThat(modelAndView.getViewName(), equalTo("instrucoes-recuperar-senha"));
        assertThat(modelAndView.getModel().get("link"), equalTo("/editar/recuperar-senha?token="
                +TOKEN+"&usuarioId="+USUARIO_ID+"&pagina="+ COMPLETAR_CADASTRO));
        assertThat(modelAndView.getModel().get("usuario"), equalTo(USUARIO));
    }

    @Test(expected = AccessDeniedException.class)
    public void deveLancarExcecaoSeNaoPermitidoAlterarUsuario() {
        when(userProfiles.temPermissaoGerenciarUsuarioOrgaoEPapel(any(), any())).thenReturn(false);
        controller.criar(FORM_USUARIO, bindingResult);
    }

    @Test
    public void naoSalvaSeFormularioPossuiErros() {
        when(userProfiles.temPermissaoGerenciarUsuarioOrgaoEPapel(any(), any())).thenReturn(true);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(factory.criarUsuario(FORM_USUARIO)).thenReturn(USUARIO);
        controller.criar(FORM_USUARIO, bindingResult);
        verify(usuarioService, never()).save(USUARIO);
    }

    @Test
    public void retornaMesmoUsuarioFormularioSeFormularioPossuiErros() {
        when(userProfiles.temPermissaoGerenciarUsuarioOrgaoEPapel(any(), any())).thenReturn(true);
        when(bindingResult.hasErrors()).thenReturn(true);
        ModelAndView modelAndView = controller.criar(FORM_USUARIO, bindingResult);
        assertThat(modelAndView.getViewName(), equalTo("cadastrar"));
    }

    @Test
    public void mostraInformacoesDoUsuarioNasIntrucoesDeRecuperarSenhas() {
        when(usuarioService.findById(USUARIO_ID)).thenReturn(USUARIO);
        ModelAndView view = controller.requisitarTrocaSenha(USUARIO_ID);
        assertThat(view.getModel().get("usuario"), equalTo(USUARIO));
    }

    @Test
    public void mostrarLinkComTokenNasIntrucoesDeRecuperarSenhas() {
        when(usuarioService.findById(USUARIO_ID)).thenReturn(USUARIO);
        when(tokenService.gerarTokenParaUsuario(USUARIO_ID)).thenReturn(TOKEN);
        ModelAndView view = controller.requisitarTrocaSenha(USUARIO_ID);
        assertThat(view.getModel().get("link"), equalTo("/editar/recuperar-senha?token="+TOKEN+
                "&usuarioId="+USUARIO_ID+"&pagina="+ RECUPERAR_SENHA));
    }
}