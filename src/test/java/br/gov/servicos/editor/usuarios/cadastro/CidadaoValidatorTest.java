package br.gov.servicos.editor.usuarios.cadastro;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CidadaoValidatorTest {
    public static final String SIAPE = "1234";
    public static final String VAZIO = "";
    @Mock
    ConstraintValidatorContext context;

    @Test
    public void campoInvalidoSeNaoForServidorETemSiape() {
        CamposServidor camposServidor = new CamposServidor().withServidor(false).withSiape(SIAPE);
        CidadaoValidator validator = new CidadaoValidator();
        assertFalse(validator.isValid(camposServidor, context));
    }

    @Test
    public void campoValidoSeForNãoForServidorComSiape() {
        CamposServidor camposServidor = new CamposServidor().withServidor(false).withSiape(VAZIO);
        CidadaoValidator validator = new CidadaoValidator();
        assertTrue(validator.isValid(camposServidor, context));
    }

}