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
    private static final String NOME_CAMPO_COM_VALOR_CPF = "cpf";
    private static final String NOME_CAMPO_MARCA_VALIDACAO_ATIVA = "ehInclusaoDeUsuario";
    private static final String CPF_FORMATADO = "123.123.123-12";

    @Mock
    private UsuarioRepository repository;

    @Mock
    private ConstraintValidatorContext context;

    @InjectMocks
    private CpfUnicoValidator validator;

    private FormularioUsuario formularioUsuario;

    @Test
    public void deveProcurarPorCpfDesformatado() {
        formularioUsuario = new FormularioUsuario();
        validator.nomeCampoComValorCpf = NOME_CAMPO_COM_VALOR_CPF;
        validator.nomeCampoMarcaSeValidacaoAtiva = NOME_CAMPO_MARCA_VALIDACAO_ATIVA;
        validator.isValid(formularioUsuario.withCpf(CPF_FORMATADO).withEhInclusaoDeUsuario(Boolean.TRUE), context);
        verify(repository).findByCpf(CPF);
    }

}