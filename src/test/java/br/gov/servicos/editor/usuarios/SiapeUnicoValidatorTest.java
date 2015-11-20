package br.gov.servicos.editor.usuarios;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintValidatorContext;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SiapeUnicoValidatorTest {
    private static final String SIAPE = "1234";
    @Mock
    ConstraintValidatorContext context;

    @Mock
    UsuarioRepository repository;

    @InjectMocks
    SiapeUnicoValidator validator;

    @Test
    public void campoSiapeValidoSeForVazio() {
        SiapeUnicoValidator validator = new SiapeUnicoValidator();
        assertTrue(validator.isValid("", context));
    }

    @Test
    public void campoSiapeValidoSeAindaNaoExistirNaBase() {
        when(repository.findBySiape(SIAPE)).thenReturn(null);
        assertTrue(validator.isValid(SIAPE, context));
    }

    @Test
    public void campoSiapeInvalidoSeJaExisteNaBase() {
        when(repository.findBySiape(SIAPE)).thenReturn(new Usuario());
        assertFalse(validator.isValid(SIAPE, context));
    }

}