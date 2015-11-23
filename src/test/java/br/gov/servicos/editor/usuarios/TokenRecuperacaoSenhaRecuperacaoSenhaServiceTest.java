package br.gov.servicos.editor.usuarios;

import br.gov.servicos.editor.usuarios.cadastro.TokenRecuperacaoSenhaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TokenRecuperacaoSenhaRecuperacaoSenhaServiceTest {

    public static final String CPF = "12312312312";
    private static final String TOKEN = "token";
    private static final java.lang.String ENCRYPTED_TOKEN = "encrypted";

    @Mock
    private GeradorToken geradorToken;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenRecuperacaoSenhaRepository repository;

    @InjectMocks
    private TokenRecuperacaoSenhaService tokenRecuperacaoSenhaService;

    @Test
    public void deveGerarTokenAleatorioEGuardarEncryptadoNoBanco() {
        when(geradorToken.gerar()).thenReturn(TOKEN);
        when(passwordEncoder.encode(TOKEN)).thenReturn(ENCRYPTED_TOKEN);
        String token = tokenRecuperacaoSenhaService.gerarParaUsuario(CPF);

        TokenRecuperacaoSenha expectedTokenRecuperacaoSenha = new TokenRecuperacaoSenha().withToken(ENCRYPTED_TOKEN).withCpf(CPF);
        verify(repository).save(expectedTokenRecuperacaoSenha);
        assertThat(token, equalTo(TOKEN));
    }


}