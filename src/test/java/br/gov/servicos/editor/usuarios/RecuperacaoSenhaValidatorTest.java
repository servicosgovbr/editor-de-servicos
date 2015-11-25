package br.gov.servicos.editor.usuarios;

import br.gov.servicos.editor.usuarios.cadastro.TokenRecuperacaoSenhaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidatorContext;

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

    @Mock
    private TokenRecuperacaoSenhaRepository repository;

    @Mock
    private ConstraintValidatorContext context;

    @InjectMocks
    RecuperacaoSenhaValidator validator;
    private TokenRecuperacaoSenha token;
    private Usuario usuario;
    private CamposVerificacaoRecuperarSenha formulario;

    @Before
    public void setUp() {
        usuario = new Usuario()
                .withCpf(CPF)
                .withId(USUARIO_ID);
        token = new TokenRecuperacaoSenha()
                .withToken(ENCRYPTED_TOKEN)
                .withUsuario(usuario);

        formulario = new CamposVerificacaoRecuperarSenha()
                .withCpf(CPF_FORMATADO)
                .withUsuarioId(USUARIO_ID.toString())
                .withToken(TOKEN);
    }

    @Test
    public void deveValidarSeUsuarioIdCpfETokenForemCompativeis() {
        when(passwordEncoder.matches(TOKEN, ENCRYPTED_TOKEN)).thenReturn(true);
        when(repository.findByUsuarioId(USUARIO_ID)).thenReturn(token);
        assertTrue(validator.isValid(formulario, context));
    }

    @Test
    public void deveInvalidarSeCpfEstiverIncorreto() {
        when(passwordEncoder.matches(TOKEN, ENCRYPTED_TOKEN)).thenReturn(true);
        Usuario usuarioComCpfDiferente = usuario.withCpf(OUTRO_CPF);
        TokenRecuperacaoSenha tokenComUsuarioDeCpfDiferente = token.withUsuario(usuarioComCpfDiferente);
        when(repository.findByUsuarioId(USUARIO_ID)).thenReturn(tokenComUsuarioDeCpfDiferente);
        assertFalse(validator.isValid(formulario, context));
    }

    @Test
    public void deveInvalidarSeTokenEstiverIncorreto() {
        when(repository.findByUsuarioId(USUARIO_ID)).thenReturn(token);
        when(passwordEncoder.matches(TOKEN, ENCRYPTED_TOKEN)).thenReturn(false);
        assertFalse(validator.isValid(formulario, context));
    }
}