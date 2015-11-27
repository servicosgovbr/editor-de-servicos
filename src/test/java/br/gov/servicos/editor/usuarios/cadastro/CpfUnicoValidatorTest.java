package br.gov.servicos.editor.usuarios.cadastro;

import br.gov.servicos.editor.usuarios.UsuarioRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintValidatorContext;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CpfUnicoValidatorTest {

    private static final String CPF = "12312312312";
    private static final String CPF_FORMATADO = "123.123.123-12";

    @Mock
    private UsuarioRepository repository;

    @Mock
    private ConstraintValidatorContext context;

    @InjectMocks
    private CpfUnicoValidator validator;

    @Test
    public void deveProcurarPorCpfDesformatado() {
        validator.isValid(CPF_FORMATADO, context);
        verify(repository).findByCpf(CPF);
    }

}