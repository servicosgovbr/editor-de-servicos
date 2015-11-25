package br.gov.servicos.editor.usuarios;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    RecuperacaoSenhaValidator validator;
    private TokenRecuperacaoSenha token;
    private FormularioRecuperarSenha formulario;
    private Usuario usuario;

    @Before
    public void setUp() {
        usuario = new Usuario()
                .withCpf(CPF)
                .withId(USUARIO_ID);
        token = new TokenRecuperacaoSenha()
                .withToken(ENCRYPTED_TOKEN)
                .withUsuario(usuario);
        formulario = new FormularioRecuperarSenha()
                .withCpf(CPF_FORMATADO)
                .withToken(TOKEN)
                .withUsuarioId(USUARIO_ID.toString());
    }

    @Test
    public void deveValidarSeUsuarioIdCpfETokenForemCompativeis() {
        when(passwordEncoder.matches(TOKEN, ENCRYPTED_TOKEN)).thenReturn(true);
        assertTrue(validator.isValid(formulario, token));
    }

    @Test
    public void deveInvalidarSeCpfEstiverIncorreto() {
        when(passwordEncoder.matches(TOKEN, ENCRYPTED_TOKEN)).thenReturn(true);
        Usuario usuarioComCpfDiferente = usuario.withCpf(OUTRO_CPF);
        TokenRecuperacaoSenha tokenComUsuarioDeCpfDiferente = token.withUsuario(usuarioComCpfDiferente);
        assertFalse(validator.isValid(formulario, tokenComUsuarioDeCpfDiferente));
    }

    @Test
    public void deveInvalidarSeTokenEstiverIncorreto() {
        when(passwordEncoder.matches(TOKEN, ENCRYPTED_TOKEN)).thenReturn(false);
        assertFalse(validator.isValid(formulario, token));
    }
}