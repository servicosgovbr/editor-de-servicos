package br.gov.servicos.editor.frontend;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.User;

import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ApiControllerTest {

    private ApiController controller;

    public static final User USUARIO = new User("Fulano de Tal", "", emptyList());

    @Before
    public void setUp() throws Exception {
        controller = new ApiController();
    }

    @Test
    public void pingDeveRetornarLoginEHorario() throws Exception {
        ApiController.Ping ping = controller.ping(USUARIO);

        assertThat(ping.getLogin(), is("Fulano de Tal"));
        assertThat(System.currentTimeMillis() - ping.getHorario(), is(lessThan(1000L)));
    }
}