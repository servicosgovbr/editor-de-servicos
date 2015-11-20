package br.gov.servicos.editor.usuarios.cadastro;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ServidorValidatorTest {
    public static final String SIAPE = "1234";
    public static final String VAZIO = "";
    @Mock
    ConstraintValidatorContext context;

    @Test
    public void campoInvalidoSeForServidorSemSiape() {
        CamposServidor camposServidor = new CamposServidor().withServidor(true).withSiape(VAZIO);
        ServidorValidator validator = new ServidorValidator();
        assertFalse(validator.isValid(camposServidor, context));
    }

    @Test
    public void campoValidoSeForServidorComSiape() {
        CamposServidor camposServidor = new CamposServidor().withServidor(true).withSiape(SIAPE);
        ServidorValidator validator = new ServidorValidator();
        assertTrue(validator.isValid(camposServidor, context));
    }

}