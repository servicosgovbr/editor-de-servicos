package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.ConteudoVersionadoFactory;
import br.gov.servicos.editor.fixtures.UserProfileConfigParaTeste;
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

@RunWith(MockitoJUnitRunner.class)
public class DespublicarPaginaControllerTest {

    @Mock
    ConteudoVersionado carta;

    @Mock
    ConteudoVersionadoFactory factory;

    UserProfileConfigParaTeste userProfiles = new UserProfileConfigParaTeste();

    @Test
    public void retornarAcessoNegadoCasoUsuarioNaoTenhaAcesso() throws ConteudoInexistenteException {
        DespublicarPaginaController controller = new DespublicarPaginaController(factory, userProfiles);
        given(factory.pagina(anyString(), any()))
                .willReturn(carta);
        userProfiles.setTemPermissaoParaOrgao(false);

        ResponseEntity resultado = controller.despublicar("servico", "");

        assertThat(resultado.getStatusCode(), equalTo(HttpStatus.FORBIDDEN));
    }

}