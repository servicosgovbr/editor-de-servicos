package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.ConteudoVersionadoFactory;
import br.gov.servicos.editor.conteudo.TipoPagina;
import br.gov.servicos.editor.git.Metadados;
import br.gov.servicos.editor.security.TipoPermissao;
import br.gov.servicos.editor.security.UserProfiles;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.access.AccessDeniedException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PublicarPaginaControllerTest {

    @Mock
    ConteudoVersionado carta;

    @Mock
    ConteudoVersionadoFactory factory;

    PublicarPaginaController controller;

    @Mock
    UserProfiles userProfiles;

    @Before
    public void setUp() throws Exception {
        controller = new PublicarPaginaController(factory, userProfiles);
        given(carta.getMetadados())
                .willReturn(new Metadados());
        given(carta.existe())
                .willReturn(true);
        given(factory.pagina(anyString(), any()))
                .willReturn(carta);
    }

    @Test
    public void publicaCartaExistente() throws Exception {
        given(userProfiles.temPermissaoParaTipoPagina(eq(TipoPermissao.PUBLICAR), any(TipoPagina.class))).willReturn(true);
        controller.publicar("servico", "");
        verify(carta).publicar(userProfiles.get());
    }

    @Test(expected = AccessDeniedException.class)
    public void retornarAcessoNegadoCasoUsuarioNaoTenhaAcesso() throws ConteudoInexistenteException {
        given(userProfiles.temPermissaoParaTipoPagina(eq(TipoPermissao.PUBLICAR), any(TipoPagina.class))).willReturn(false);
        given(userProfiles.temPermissaoParaTipoPaginaOrgaoEspecifico(eq(TipoPermissao.PUBLICAR), any(TipoPagina.class), anyString())).willReturn(false);
        controller.publicar("servico", "");
    }

}