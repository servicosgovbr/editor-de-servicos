package br.gov.servicos.editor.usuarios;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ConfirmacaoSenhaValidatorTest {
    public static final String SENHA = "1234";
    public static final String OUTRA_SENHA = "9999";
    @Mock
    ConstraintValidatorContext context;

    @Test
    public void campoValidoSeSenhasSaoIguais() {
        ConfirmacaoSenhaValidator validator = new ConfirmacaoSenhaValidator();
        CamposSenha camposSenha = new CamposSenha().withSenha(SENHA).withConfirmacaoSenha(SENHA);
        assertTrue(validator.isValid(camposSenha, context));
    }

    @Test
    public void campoInvalidoSeSenhasSaoDiferentes() {
        ConfirmacaoSenhaValidator validator = new ConfirmacaoSenhaValidator();
        CamposSenha camposSenha = new CamposSenha().withSenha(SENHA).withConfirmacaoSenha(OUTRA_SENHA);
        assertFalse(validator.isValid(camposSenha, context));
    }
}