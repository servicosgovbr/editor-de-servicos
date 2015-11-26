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
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RecuperacaoSenhaServiceTest {

    private static final String TOKEN = "token";
    private static final java.lang.String ENCRYPTED_TOKEN = "encrypted";
    private static final Long USUARIO_ID = 12341234L;
    private static final String SENHA = "12341234";
    private static final String ENCRYPTED_SENHA = "******";
    private static final Long TOKEN_ID = 1L;
    public static final int MAX = 10;

    @Mock
    private GeradorToken geradorToken;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenRecuperacaoSenhaRepository repository;

    @Mock
    private RecuperacaoSenhaValidator validator;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private RecuperacaoSenhaService recuperacaoSenhaService;

    @Before
    public void setUp() {
        when(passwordEncoder.encode(TOKEN)).thenReturn(ENCRYPTED_TOKEN);
        when(passwordEncoder.encode(SENHA)).thenReturn(ENCRYPTED_SENHA);
        ReflectionTestUtils.setField(recuperacaoSenhaService, "maxTentativasToken", MAX);
    }


    @Test
    public void deveGerarTokenAleatorioEGuardarEncryptadoNoBanco() {
        when(geradorToken.gerar()).thenReturn(TOKEN);
        String token = recuperacaoSenhaService.gerarTokenParaUsuario(USUARIO_ID.toString());

        TokenRecuperacaoSenha expectedTokenRecuperacaoSenha = new TokenRecuperacaoSenha()
                .withToken(ENCRYPTED_TOKEN)
                .withUsuario(new Usuario().withId(USUARIO_ID))
                .withTentativas(0);
        verify(repository).save(refEq(expectedTokenRecuperacaoSenha, "dataCriacao"));
        assertThat(token, equalTo(TOKEN));
    }

    @Test
    public void deveSalvarSenhaSeTokenForValido() throws TokenInvalido {
        FormularioRecuperarSenha formulario = criarFormulario(USUARIO_ID, SENHA);
        Usuario usuario = new Usuario();
        TokenRecuperacaoSenha token = new TokenRecuperacaoSenha().withUsuario(usuario);

        when(repository.findByUsuarioId(USUARIO_ID)).thenReturn(token);

        when(validator.isValid(formulario, token)).thenReturn(true);

        recuperacaoSenhaService.trocarSenha(formulario);
        verify(usuarioRepository).save(usuario.withSenha(ENCRYPTED_SENHA));
    }

    @Test
    public void naoDeveSalvarSenhaSeTokenForInvalido() {
        FormularioRecuperarSenha formulario = criarFormulario(USUARIO_ID, SENHA);
        Usuario usuario = new Usuario();
        TokenRecuperacaoSenha token = new TokenRecuperacaoSenha().withUsuario(usuario).withTentativas(0);
        when(repository.findByUsuarioId(USUARIO_ID)).thenReturn(token);
        when(validator.isValid(formulario, token)).thenReturn(false);

        try {
            recuperacaoSenhaService.trocarSenha(formulario);
            fail();
        } catch (TokenInvalido e) {
            verify(usuarioRepository, never()).save(usuario.withSenha(ENCRYPTED_SENHA));
        }
    }

    @Test
    public void deveIncrementarNumeroDeTentativasSeTokenForInvalido() {
        FormularioRecuperarSenha formulario = criarFormulario(USUARIO_ID, SENHA);
        Usuario usuario = new Usuario();
        TokenRecuperacaoSenha token = new TokenRecuperacaoSenha()
                .withUsuario(usuario)
                .withTentativas(0);
        TokenRecuperacaoSenha expectedToken = new TokenRecuperacaoSenha()
                .withUsuario(usuario)
                .withTentativas(1);

        when(repository.findByUsuarioId(USUARIO_ID)).thenReturn(token);
        when(validator.isValid(formulario, token)).thenReturn(false);

        try {
            recuperacaoSenhaService.trocarSenha(formulario);
        } catch (TokenInvalido e) {
            verify(repository).save(expectedToken);
        }
    }

    @Test
    public void deveLancarExcecaoCasoTokenSejaInvalidoComMensagemDizendoQuantasTentativasEstaoFaltando() {
        FormularioRecuperarSenha formulario = criarFormulario(USUARIO_ID, SENHA);
        Usuario usuario = new Usuario();
        TokenRecuperacaoSenha token = new TokenRecuperacaoSenha()
                .withUsuario(usuario)
                .withTentativas(0);

        when(repository.findByUsuarioId(USUARIO_ID)).thenReturn(token);
        when(validator.isValid(formulario, token)).thenReturn(false);

        try {
            recuperacaoSenhaService.trocarSenha(formulario);
            fail();
        } catch(TokenInvalido e) {
            assertThat(e.getTentativasSobrando(), equalTo(MAX-1));
        }
    }


    @Test
    public void deveDeletarTokenCasoSenhaTenhaSidoTrocada() throws TokenInvalido {
        FormularioRecuperarSenha formulario = criarFormulario(USUARIO_ID, SENHA);
        Usuario usuario = new Usuario();
        TokenRecuperacaoSenha token = new TokenRecuperacaoSenha()
                .withUsuario(usuario)
                .withId(TOKEN_ID);
        when(repository.findByUsuarioId(USUARIO_ID)).thenReturn(token);
        when(validator.isValid(formulario, token)).thenReturn(true);

        recuperacaoSenhaService.trocarSenha(formulario);

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