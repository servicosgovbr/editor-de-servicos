package br.gov.servicos.editor.security;

import br.gov.servicos.editor.conteudo.TipoPagina;
import br.gov.servicos.editor.usuarios.Usuario;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.omg.CORBA.Object;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginUserProfilesTest {

    private static final String EMAIL = "email@institucional.gov.br";
    public static final String NOME = "Editor de Serviço";
    private static final String ORGAO_ID = "orgaoId";
    private static final String OUTRO_ORGAO = "outroOrgaoId";
    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private LoginUserProfiles userProfiles;

    @Before
    public void setUp() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void deveEnviarUserProfileVazioCasoUsuarioNaoEstejaLogado() {
        Object dummyObject = mock(Object.class);
        when(authentication.getPrincipal()).thenReturn(dummyObject);
        assertThat(userProfiles.get(), equalTo(new UserProfile()));
    }

    @Test
    public void deveEnviarUserProfileComDadosDeUsuarioLogado() {
        Usuario usuario = new Usuario().withEmailPrimario(EMAIL).withNome(NOME);
        when(authentication.getPrincipal()).thenReturn(usuario);
        UserProfile actual = userProfiles.get();
        assertThat(actual.getEmail(), equalTo(EMAIL));
        assertThat(actual.getName(), equalTo(NOME));
        assertThat(actual.getId(), equalTo(EMAIL));
    }

    @Test
    public void deveRetornarTrueSeTipoDePaginaForDiferenteDeServicoOuOrgao() {
        assertTrue(userProfiles.temPermissaoParaOrgao(TipoPagina.PAGINA_TEMATICA, ""));
    }

    @Test
    public void deveRetornarTrueSeTipoDePaginaForServicoOuOrgaoEOrgaoIdForIgualAoDoUsuario() {
        Usuario usuario = new Usuario().withSiorg(ORGAO_ID);
        when(authentication.getPrincipal()).thenReturn(usuario);
        assertTrue(userProfiles.temPermissaoParaOrgao(TipoPagina.SERVICO, ORGAO_ID));
    }

    @Test
    public void deveRetornarFalseSeTipoDePaginaForServicoOuOrgaoMasOrgaoDiferenteDeUsuario() {
        Usuario usuario = new Usuario().withSiorg(ORGAO_ID);
        when(authentication.getPrincipal()).thenReturn(usuario);
        assertFalse(userProfiles.temPermissaoParaOrgao(TipoPagina.SERVICO, OUTRO_ORGAO));
    }


}