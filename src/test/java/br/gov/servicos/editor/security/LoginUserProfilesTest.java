package br.gov.servicos.editor.security;

import br.gov.servicos.editor.usuarios.Papel;
import br.gov.servicos.editor.usuarios.Usuario;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static br.gov.servicos.editor.security.TipoPermissao.CADASTRAR;
import static br.gov.servicos.editor.security.TipoPermissao.CADASTRAR_OUTROS_ORGAOS;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginUserProfilesTest {

    private static final String EMAIL = "email@institucional.gov.br";
    private static final String NOME = "Editor de Serviço";
    private static final String ORGAO_ID = "orgaoId";
    private static final String OUTRO_ORGAO = "outroOrgaoId";
    private static final String NOME_PAPEL = "EDITOR";
    private static final Collection<Permissao> PERMISSOES = Collections.emptyList();


    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private LoginUserProfiles userProfiles;

    @Mock
    private GerenciadorPermissoes gerenciadorPermissoes;



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
        Papel papel = new Papel();
        papel.setNome(NOME_PAPEL);
        Usuario usuario = new Usuario().withEmailPrimario(EMAIL).withNome(NOME).withPapel(papel);
        usuario.setGerenciadorPermissoes(gerenciadorPermissoes);
        when(gerenciadorPermissoes.getPermissoes(Matchers.anyString())).thenReturn(PERMISSOES);
        when(authentication.getPrincipal()).thenReturn(usuario);
        UserProfile actual = userProfiles.get();
        assertThat(actual.getEmail(), equalTo(EMAIL));
        assertThat(actual.getName(), equalTo(NOME));
        assertThat(actual.getId(), equalTo(EMAIL));
    }

    @Test
    public void deveValidarSeUsuarioTiverPermissaoParaOperacao() {
        Usuario usuario = mock(Usuario.class);
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(usuario.temPermissaoComOrgao(any(), any())).thenReturn(true);
        assertTrue(userProfiles.temPermissaoParaOrgao(TipoPermissao.PUBLICAR, ORGAO_ID));
    }

    @Test
    public void deveInvalidarSeUsuarioNãoTiverPermissaoParaOperacao() {
        Usuario usuario = mock(Usuario.class);
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(usuario.temPermissaoComOrgao(any(), any())).thenReturn(false);
        assertFalse(userProfiles.temPermissaoParaOrgao(TipoPermissao.PUBLICAR, OUTRO_ORGAO));
    }

    @Test
    public void deveInvalidarSeUsuarioNaoTemPermissaoParaCadastrarPapel() {
        Usuario usuario = mock(Usuario.class);
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(usuario.temPermissao(CADASTRAR.comPapel("PUBLICADOR"))).thenReturn(false);
        assertFalse(userProfiles.temPermissaoGerenciarUsuarioOrgaoEPapel(ORGAO_ID, "PUBLICADOR"));
    }

    @Test
    public void deveInvalidarSeSiorgForDiferenteDeOrgaoId() {
        Usuario usuario = mock(Usuario.class);
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(usuario.temPermissao(CADASTRAR.comPapel("PUBLICADOR"))).thenReturn(true);
        when(usuario.getSiorg()).thenReturn(OUTRO_ORGAO);
        assertFalse(userProfiles.temPermissaoGerenciarUsuarioOrgaoEPapel(ORGAO_ID, "PUBLICADOR"));
    }

    @Test
    public void deveValidarSeSiorForIgualAoOrgaoId() {
        Usuario usuario = mock(Usuario.class);
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(usuario.temPermissao(CADASTRAR.comPapel("PUBLICADOR"))).thenReturn(true);
        when(usuario.getSiorg()).thenReturn(ORGAO_ID);
        assertTrue(userProfiles.temPermissaoGerenciarUsuarioOrgaoEPapel(ORGAO_ID, "PUBLICADOR"));
    }

    @Test
    public void deveValidarSeUsuarioTemPermissaoDeCadastrarOutrosOrgaosMesmoSeSiorgForDiferenteDeOrgaoId() {
        Usuario usuario = mock(Usuario.class);
        when(authentication.getPrincipal()).thenReturn(usuario);
        when(usuario.temPermissao(CADASTRAR.comPapel("PUBLICADOR"))).thenReturn(true);
        when(usuario.temPermissao(CADASTRAR_OUTROS_ORGAOS.getNome())).thenReturn(true);
        when(usuario.getSiorg()).thenReturn(OUTRO_ORGAO);
        assertTrue(userProfiles.temPermissaoGerenciarUsuarioOrgaoEPapel(ORGAO_ID, "PUBLICADOR"));
    }

}