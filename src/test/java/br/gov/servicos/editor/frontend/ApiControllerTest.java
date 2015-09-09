package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.cartas.ListaDeConteudo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;

import static java.util.Collections.emptyList;
import static java.util.Optional.of;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApiControllerTest {

    public static final User USUARIO = new User("Fulano de Tal", "", emptyList());

    @Mock
    VCGE vcge;

    @Mock
    Orgaos orgaos;

    @Mock
    Siorg siorg;

    @Mock
    ListaDeConteudo listaDeConteudo;

    ApiController controller;

    @Before
    public void setUp() throws Exception {
        controller = new ApiController(vcge, orgaos, siorg, listaDeConteudo);
    }

    @Test
    public void pingDeveRetornarLoginEHorario() throws Exception {
        Ping ping = controller.ping(USUARIO);

        assertThat(ping.getLogin(), is("Fulano de Tal"));
        assertThat(System.currentTimeMillis() - ping.getHorario(), is(lessThan(1000L)));
    }

    @Test
    public void listaOrgaosDisponiveis() throws Exception {
        controller.orgaos("Instituto");
        verify(orgaos).get("Instituto");
    }

    @Test
    public void listaCategoriasDoVcgeDisponiveis() throws Exception {
        controller.vcge();
        verify(vcge).get();
    }

    @Test
    public void buscaOrgaosDisponivel() throws Exception {
        when(siorg.nomeDoOrgao("http://estruturaorganizacional.dados.gov.br/doc/unidade-organizacional/404")).thenReturn(of(""));
        controller.orgao("http://estruturaorganizacional.dados.gov.br/doc/unidade-organizacional/404");
        verify(siorg).nomeDoOrgao("http://estruturaorganizacional.dados.gov.br/doc/unidade-organizacional/404");
    }
}