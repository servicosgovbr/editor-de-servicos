package br.gov.servicos.editor.usuarios;

import br.gov.servicos.editor.usuarios.cadastro.CamposSenha;
import br.gov.servicos.editor.usuarios.cadastro.TokenRecuperacaoSenhaRepository;
import org.junit.Before;
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
public class RecuperacaoSenhaServiceTest {

    private static final String TOKEN = "token";
    private static final java.lang.String ENCRYPTED_TOKEN = "encrypted";
    private static final Long USUARIO_ID = 12341234L;
    private static final String SENHA = "12341234";
    private static final String ENCRYPTED_SENHA = "******";
    private static final Long TOKEN_ID = 1L;

    @Mock
    private GeradorToken geradorToken;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenRecuperacaoSenhaRepository repository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private RecuperacaoSenhaService recuperacaoSenhaService;

    @Before
    public void setUp() {
        when(passwordEncoder.encode(TOKEN)).thenReturn(ENCRYPTED_TOKEN);
        when(passwordEncoder.encode(SENHA)).thenReturn(ENCRYPTED_SENHA);
    }


    @Test
    public void deveGerarTokenAleatorioEGuardarEncryptadoNoBanco() {
        when(geradorToken.gerar()).thenReturn(TOKEN);
        String token = recuperacaoSenhaService.gerarTokenParaUsuario(USUARIO_ID.toString());

        TokenRecuperacaoSenha expectedTokenRecuperacaoSenha = new TokenRecuperacaoSenha().
                withToken(ENCRYPTED_TOKEN).
                withUsuario(new Usuario().withId(USUARIO_ID));
        verify(repository).save(refEq(expectedTokenRecuperacaoSenha, "dataCriacao"));
        assertThat(token, equalTo(TOKEN));
    }

    @Test
    public void deveSalvarSenhaEDeletarToken() {
        FormularioRecuperarSenha formulario = criarFormulario(USUARIO_ID, SENHA);
        Usuario usuario = new Usuario();
        TokenRecuperacaoSenha token = new TokenRecuperacaoSenha()
                .withUsuario(usuario)
                .withId(TOKEN_ID);
        when(repository.findByUsuarioId(USUARIO_ID)).thenReturn(token);

        recuperacaoSenhaService.trocarSenha(formulario);

        verify(usuarioRepository).save(usuario.withSenha(ENCRYPTED_SENHA));
        verify(repository).delete(TOKEN_ID);
    }


    private FormularioRecuperarSenha criarFormulario(Long usuarioId, String senha) {
        CamposVerificacaoRecuperarSenha camposVerificacaoRecuperarSenha = new CamposVerificacaoRecuperarSenha()
                .withUsuarioId(usuarioId.toString());
        return new FormularioRecuperarSenha()
                    .withCamposVerificacaoRecuperarSenha(camposVerificacaoRecuperarSenha)
                    .withCamposSenha(new CamposSenha().withSenha(senha));
    }

}