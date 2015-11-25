package br.gov.servicos.editor.usuarios;

import br.gov.servicos.editor.usuarios.cadastro.FormularioUsuario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

import static java.lang.Long.valueOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GerenciarUsuarioControllerTest {

    public static final String CPF = "12312312319";
    private static final String TOKEN = "token";
    private static final String USUARIO_ID = "123412341234";
    private FormularioUsuario FORM_USUARIO = new FormularioUsuario();
    private Usuario USUARIO = new Usuario().withCpf(CPF);

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private UsuarioFactory factory;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RecuperacaoSenhaService tokenService;

    @InjectMocks
    private GerenciarUsuarioController controller;

    @Test
    public void salvaNovoUsuario() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(factory.criarUsuario(FORM_USUARIO)).thenReturn(USUARIO);
        controller.criar(FORM_USUARIO, bindingResult);
        verify(usuarioService).save(USUARIO);
    }

    @Test
    public void retornarNovoUsuarioFormularioCasoCadastroTenhaSidoFeito() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(factory.criarUsuario(FORM_USUARIO)).thenReturn(USUARIO);
        String endereco = controller.criar(FORM_USUARIO, bindingResult);
        assertThat(endereco, equalTo("redirect:/editar/usuarios/usuario?sucesso"));

    }

    @Test
    public void naoSalvaSeFormularioPossuiErros() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(factory.criarUsuario(FORM_USUARIO)).thenReturn(USUARIO);
        controller.criar(FORM_USUARIO, bindingResult);
        verify(usuarioService, never()).save(USUARIO);
    }

    @Test
    public void retornaMesmoUsuarioFormularioSeFormularioPossuiErros() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(factory.criarUsuario(FORM_USUARIO)).thenReturn(USUARIO);
        String endereco = controller.criar(FORM_USUARIO, bindingResult);
        assertThat(endereco, equalTo("cadastrar"));
    }

    @Test
    public void mostraInformacoesDoUsuarioNasIntrucoesDeRecuperarSenhas() {
        when(usuarioService.findById(USUARIO_ID)).thenReturn(USUARIO);
        ModelAndView view = controller.requisitarTrocaSenha(USUARIO_ID);
        assertThat(view.getModel().get("usuario"), equalTo(USUARIO));
    }

    @Test
    public void mostrarLinkComTokenNasIntrucoesDeRecuperarSenhas() {
        when(tokenService.gerarTokenParaUsuario(USUARIO_ID)).thenReturn(TOKEN);
        ModelAndView view = controller.requisitarTrocaSenha(USUARIO_ID);
        assertThat(view.getModel().get("link"), equalTo("/editar/recuperar-senha?token="+TOKEN+"&usuarioId="+USUARIO_ID));
    }

    @Test
    public void deveSalvarNovaSenhaSeFormularioNãoPossuirErros() {
        CamposVerificacaoRecuperarSenha camposVerificacaoRecuperarSenha = new CamposVerificacaoRecuperarSenha()
                .withUsuarioId(USUARIO_ID);
        FormularioRecuperarSenha formulario = new FormularioRecuperarSenha()
                .withCamposVerificacaoRecuperarSenha(camposVerificacaoRecuperarSenha);
        when(bindingResult.hasErrors()).thenReturn(false);
        controller.recuperarSenha(formulario, bindingResult);
        verify(tokenService).trocarSenha(formulario);
    }

    @Test
    public void deveNotificarTokenDeFalhaNaTentativaDeTrocarSenha() {
        CamposVerificacaoRecuperarSenha camposVerificacaoRecuperarSenha = new CamposVerificacaoRecuperarSenha()
                .withUsuarioId(USUARIO_ID);
        FormularioRecuperarSenha formulario = new FormularioRecuperarSenha()
                .withCamposVerificacaoRecuperarSenha(camposVerificacaoRecuperarSenha);
        when(bindingResult.hasErrors()).thenReturn(true);
        controller.recuperarSenha(formulario, bindingResult);
        verify(tokenService).falhaNaVerificacao(valueOf(USUARIO_ID));
    }

    @Test
    public void naoDeveNotificarTokenSeErroNaoForRelactionadoAVerificacaoCpfToken() {
        CamposVerificacaoRecuperarSenha camposVerificacaoRecuperarSenha = new CamposVerificacaoRecuperarSenha()
                .withUsuarioId(USUARIO_ID);
        FormularioRecuperarSenha formulario = new FormularioRecuperarSenha()
                .withCamposVerificacaoRecuperarSenha(camposVerificacaoRecuperarSenha);
        when(bindingResult.hasErrors()).thenReturn(true);
        FieldError fieldError = mock(FieldError.class);
        when(fieldError.getCode()).thenReturn("OutraValidacao");
        when(bindingResult.getFieldError(any())).thenReturn(fieldError);
        controller.recuperarSenha(formulario, bindingResult);
        verify(tokenService, never()).falhaNaVerificacao(valueOf(USUARIO_ID));
    }

}