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
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TokenRecuperacaoSenhaServiceTest {

    private static final String TOKEN = "token";
    private static final java.lang.String ENCRYPTED_TOKEN = "encrypted";
    private static final Long USUARIO_ID = 12341234l;

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
        String token = tokenRecuperacaoSenhaService.gerarParaUsuario(USUARIO_ID.toString());

        TokenRecuperacaoSenha expectedTokenRecuperacaoSenha = new TokenRecuperacaoSenha().
                withToken(ENCRYPTED_TOKEN).
                withUsuarioId(USUARIO_ID);
        verify(repository).save(refEq(expectedTokenRecuperacaoSenha, "dataCriacao"));
        assertThat(token, equalTo(TOKEN));
    }


}