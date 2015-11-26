package br.gov.servicos.editor.usuarios.recuperarsenha;

import br.gov.servicos.editor.usuarios.Usuario;
import br.gov.servicos.editor.usuarios.token.Token;
import br.gov.servicos.editor.usuarios.token.TokenError;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static java.time.Clock.systemUTC;
import static java.time.LocalDateTime.now;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class RecuperacaoSenhaValidatorTest {
private static final String TOKEN = "token";
    private static final java.lang.String ENCRYPTED_TOKEN = "encrypted";
    private static final Long USUARIO_ID = 12341234L;
    private static final String CPF = "12312312312";
    private static final String CPF_FORMATADO = "123.123.123-12";
    private static final String OUTRO_CPF = "9999999999";
    private static final Integer MAX_HORAS = 24;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RecuperacaoSenhaValidator validator;
    private Token token;
    private FormularioRecuperarSenha formulario;
    private Usuario usuario;

    @Before
    public void setUp() {
        usuario = new Usuario()
                .withCpf(CPF)
                .withId(USUARIO_ID);
        token = new Token()
                .withToken(ENCRYPTED_TOKEN)
                .withUsuario(usuario)
                .withDataCriacao(now(systemUTC()));

        CamposVerificacaoRecuperarSenha camposVerificacaoRecuperarSenha = new CamposVerificacaoRecuperarSenha()
                .withCpf(CPF_FORMATADO)
                .withUsuarioId(USUARIO_ID.toString())
                .withToken(TOKEN);
        formulario = new FormularioRecuperarSenha()
                .withCamposVerificacaoRecuperarSenha(camposVerificacaoRecuperarSenha);

        ReflectionTestUtils.setField(validator, "maxHorasToken", MAX_HORAS);
    }

    @Test
    public void deveValidarSeUsuarioIdCpfETokenForemCompativeis() {
        when(passwordEncoder.matches(TOKEN, ENCRYPTED_TOKEN)).thenReturn(true);
        assertFalse(validator.hasError(formulario, token).isPresent());
    }

    @Test
    public void deveInvalidarSeCpfEstiverIncorreto() {
        when(passwordEncoder.matches(TOKEN, ENCRYPTED_TOKEN)).thenReturn(true);
        Usuario usuarioComCpfDiferente = usuario.withCpf(OUTRO_CPF);
        Token tokenComUsuarioDeCpfDiferente = token.withUsuario(usuarioComCpfDiferente);
        assertTrue(validator.hasError(formulario, tokenComUsuarioDeCpfDiferente).isPresent());
    }

    @Test
    public void deveInvalidarSeTokenEstiverIncorreto() {
        when(passwordEncoder.matches(TOKEN, ENCRYPTED_TOKEN)).thenReturn(false);
        assertTrue(validator.hasError(formulario, token).isPresent());
    }

    @Test
    public void deveInvalidarSeTokenEstiverExpirado() {
        when(passwordEncoder.matches(TOKEN, ENCRYPTED_TOKEN)).thenReturn(true);
        Token tokenExpirado = token.withDataCriacao(now(systemUTC()).minusHours(MAX_HORAS + 1));
        assertThat(validator.hasError(formulario, tokenExpirado).get(), equalTo(TokenError.EXPIRADO));
    }
}