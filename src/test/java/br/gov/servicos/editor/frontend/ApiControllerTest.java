package br.gov.servicos.editor.frontend;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;

import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ApiControllerTest {

    public static final User USUARIO = new User("Fulano de Tal", "", emptyList());

    @Mock
    VCGE vcge;

    @Mock
    Orgaos orgaos;

    ApiController controller;

    @Before
    public void setUp() throws Exception {
        controller = new ApiController(vcge, orgaos);
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
}