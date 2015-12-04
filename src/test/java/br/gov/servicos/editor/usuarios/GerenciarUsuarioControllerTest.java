package br.gov.servicos.editor.usuarios;

import br.gov.servicos.editor.security.UserProfiles;
import br.gov.servicos.editor.usuarios.cadastro.FormularioUsuario;
import br.gov.servicos.editor.usuarios.recuperarsenha.CamposVerificacaoRecuperarSenha;
import br.gov.servicos.editor.usuarios.recuperarsenha.FormularioRecuperarSenha;
import br.gov.servicos.editor.usuarios.recuperarsenha.RecuperacaoSenhaService;
import br.gov.servicos.editor.usuarios.token.CpfTokenInvalido;
import br.gov.servicos.editor.usuarios.token.TokenExpirado;
import br.gov.servicos.editor.usuarios.token.TokenInvalido;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
    private static final String PAGINA = "recuperarSenha";
    private FormularioUsuario FORM_USUARIO = new FormularioUsuario();
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

    @InjectMocks
    private GerenciarUsuarioController controller;

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

    @Test
    public void deveTentarSalvarNovaSenhaSeFormularioNãoPossuirErrosBasicos() throws TokenInvalido {
        CamposVerificacaoRecuperarSenha camposVerificacaoRecuperarSenha = new CamposVerificacaoRecuperarSenha()
                .withUsuarioId(USUARIO_ID);
        FormularioRecuperarSenha formulario = new FormularioRecuperarSenha()
                .withCamposVerificacaoRecuperarSenha(camposVerificacaoRecuperarSenha);
        when(tokenService.trocarSenha(formulario)).thenReturn(USUARIO);


        when(bindingResult.hasErrors()).thenReturn(false);
        controller.recuperarSenha(PAGINA, formulario, bindingResult);
        verify(tokenService).trocarSenha(formulario);
    }

    @Test
    public void deveAdicionarErroAResultBidingCasoTokenEstejaInvalido() throws TokenInvalido {
        FormularioRecuperarSenha formulario = new FormularioRecuperarSenha();
        when(bindingResult.hasErrors()).thenReturn(false);
        int tentativasSobrando = 3;
        doThrow(new CpfTokenInvalido(tentativasSobrando)).when(tokenService).trocarSenha(formulario);

        ModelAndView endereco = controller.recuperarSenha(PAGINA, formulario, bindingResult);

        FieldError fieldError = new FieldError(FormularioRecuperarSenha.NOME_CAMPO, CamposVerificacaoRecuperarSenha.NOME,
                "O CPF informado não é compatível com o cadastrado. Você possui mais "+tentativasSobrando+" tentativas.");
        verify(bindingResult).addError(fieldError);
        assertThat(endereco.getViewName(), equalTo("recuperar-senha"));
    }

    @Test
    public void deveAdicionarErroDeTokenBloqueadoAoResultBidingCasoTokenEstejaUltrapassadoNumeroDeTentativas() throws TokenInvalido {
        FormularioRecuperarSenha formulario = new FormularioRecuperarSenha();
        when(bindingResult.hasErrors()).thenReturn(false);
        int tentativasSobrando = 0;
        doThrow(new CpfTokenInvalido(tentativasSobrando)).when(tokenService).trocarSenha(formulario);

        controller.recuperarSenha(PAGINA, formulario, bindingResult);

        FieldError fieldError = new FieldError(FormularioRecuperarSenha.NOME_CAMPO, CamposVerificacaoRecuperarSenha.NOME,
                "O CPF informado não é compatível com o cadastrado e este link foi bloqueado. " +
                        "Entre em contato com o responsável pelo seu órgão para solicitar um novo link..");
        verify(bindingResult).addError(fieldError);
    }

    @Test
    public void deveAdicionarErroDeTokenExpiraCasoTokenEstejaExpirado() throws TokenInvalido {
        FormularioRecuperarSenha formulario = new FormularioRecuperarSenha();
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new TokenExpirado()).when(tokenService).trocarSenha(formulario);

        controller.recuperarSenha(PAGINA, formulario, bindingResult);

        FieldError fieldError = new FieldError(FormularioRecuperarSenha.NOME_CAMPO, CamposVerificacaoRecuperarSenha.NOME,
                "Este link não é válido. Solicite um novo link para alterar sua senha.");
        verify(bindingResult).addError(fieldError);
    }
}