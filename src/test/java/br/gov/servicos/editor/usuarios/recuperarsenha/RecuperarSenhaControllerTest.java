package br.gov.servicos.editor.usuarios.recuperarsenha;

import br.gov.servicos.editor.usuarios.Papel;
import br.gov.servicos.editor.usuarios.Usuario;
import br.gov.servicos.editor.usuarios.token.CpfTokenInvalido;
import br.gov.servicos.editor.usuarios.token.TokenExpirado;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RecuperarSenhaControllerTest {
    static final String USUARIO_ID = "123412341234";
    static final String PAGINA = "recuperarSenha";
    static final String CPF = "12312312319";
    static final Usuario USUARIO = new Usuario().withCpf(CPF).withId(Long.valueOf(USUARIO_ID)).withPapel(new Papel());

    @Mock
    private RecuperacaoSenhaService tokenService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private RecuperarSenhaController controller;

    @Test
    public void deveTentarSalvarNovaSenhaSeFormularioNãoPossuirErrosBasicos() throws CpfTokenInvalido, TokenExpirado {
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
    public void deveAdicionarErroAResultBidingCasoTokenEstejaInvalido() throws CpfTokenInvalido, TokenExpirado {
        FormularioRecuperarSenha formulario = new FormularioRecuperarSenha();
        when(bindingResult.hasErrors()).thenReturn(false);
        int tentativasSobrando = 3;
        doThrow(new CpfTokenInvalido(tentativasSobrando)).when(tokenService).trocarSenha(formulario);

        ModelAndView endereco = controller.recuperarSenha(PAGINA, formulario, bindingResult);

        FieldError fieldError = new FieldError(FormularioRecuperarSenha.NOME_CAMPO, CamposVerificacaoRecuperarSenha.NOME,
                "O CPF informado não é compatível com o cadastrado. Você possui mais " + tentativasSobrando + " tentativas.");
        verify(bindingResult).addError(fieldError);
        assertThat(endereco.getViewName(), equalTo("recuperar-senha"));
    }

    @Test
    public void deveAdicionarErroDeTokenBloqueadoAoResultBidingCasoTokenEstejaUltrapassadoNumeroDeTentativas() throws CpfTokenInvalido, TokenExpirado {
        FormularioRecuperarSenha formulario = new FormularioRecuperarSenha();
        when(bindingResult.hasErrors()).thenReturn(false);
        int tentativasSobrando = 0;
        doThrow(new CpfTokenInvalido(tentativasSobrando)).when(tokenService).trocarSenha(formulario);

        controller.recuperarSenha(PAGINA, formulario, bindingResult);

        FieldError fieldError = new FieldError(FormularioRecuperarSenha.NOME_CAMPO, CamposVerificacaoRecuperarSenha.NOME,
                "O CPF informado não é compatível com o cadastrado e este link foi bloqueado. " +
                        "Entre em contato com o responsável pelo seu órgão para solicitar um novo link.");
        verify(bindingResult).addError(fieldError);
    }

    @Test
    public void deveAdicionarErroDeTokenExpiraCasoTokenEstejaExpirado() throws CpfTokenInvalido, TokenExpirado {
        FormularioRecuperarSenha formulario = new FormularioRecuperarSenha();
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new TokenExpirado()).when(tokenService).trocarSenha(formulario);

        controller.recuperarSenha(PAGINA, formulario, bindingResult);

        FieldError fieldError = new FieldError(FormularioRecuperarSenha.NOME_CAMPO, CamposVerificacaoRecuperarSenha.NOME,
                "Este link não é válido. Solicite um novo link para alterar sua senha.");
        verify(bindingResult).addError(fieldError);
    }

}