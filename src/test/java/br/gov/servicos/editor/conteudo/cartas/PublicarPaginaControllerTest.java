package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.ConteudoVersionadoFactory;
import br.gov.servicos.editor.fixtures.UserProfileConfigParaTeste;
import br.gov.servicos.editor.git.Metadados;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PublicarPaginaControllerTest {

    @Mock
    ConteudoVersionado carta;

    @Mock
    ConteudoVersionadoFactory factory;

    PublicarPaginaController controller;

    UserProfileConfigParaTeste userProfiles = new UserProfileConfigParaTeste();

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
        controller.publicar("servico", "");
        verify(carta).publicar(userProfiles.get());
    }

    @Test
    public void retornarAcessoNegadoCasoUsuarioNaoTenhaAcesso() throws ConteudoInexistenteException {
        userProfiles.setTemPermissaoParaOrgao(false);
        ResponseEntity resultado = controller.publicar("servico", "");
        assertThat(resultado.getStatusCode(), equalTo(HttpStatus.FORBIDDEN));
    }

}