package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.ConteudoVersionadoFactory;
import br.gov.servicos.editor.conteudo.TipoPagina;
import br.gov.servicos.editor.security.TipoPermissao;
import br.gov.servicos.editor.security.UserProfiles;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.access.AccessDeniedException;

import java.util.function.Supplier;

import static br.gov.servicos.editor.conteudo.TipoPagina.SERVICO;
import static br.gov.servicos.editor.utils.TestData.PROFILE;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ExcluirPaginaControllerTest {

    @Mock
    ConteudoVersionado carta;

    @Mock
    UserProfiles userProfiles;

    @Mock
    ConteudoVersionadoFactory factory;

    @Captor
    ArgumentCaptor<Supplier<Void>> captor;

    ExcluirPaginaController controller;

    @Before
    public void setUp() throws Exception {
        given(factory.pagina(anyString(), eq(SERVICO)))
                .willReturn(carta);
        given(carta.existe())
                .willReturn(true);

        controller = new ExcluirPaginaController(userProfiles, factory);
    }

    @Test
    public void removeCartaExistente() throws Exception {
        given(userProfiles.temPermissaoParaTipoPagina(eq(TipoPermissao.EXCLUIR), any(TipoPagina.class))).willReturn(true);
        given(userProfiles.get())
                .willReturn(PROFILE);

        controller.remover("servico", "");
        verify(carta).remover(PROFILE);
    }

    @Test(expected = AccessDeniedException.class)
    public void retornarAcessoNegadoCasoUsuarioNaoTenhaAcesso() throws ConteudoInexistenteException {
        given(userProfiles.temPermissaoParaTipoPagina(eq(TipoPermissao.EXCLUIR), any(TipoPagina.class))).willReturn(false);
        given(userProfiles.temPermissaoParaTipoPaginaOrgaoEspecifico(eq(TipoPermissao.PUBLICAR), any(TipoPagina.class), anyString())).willReturn(false);
        controller.remover("servico", "");
    }

}
