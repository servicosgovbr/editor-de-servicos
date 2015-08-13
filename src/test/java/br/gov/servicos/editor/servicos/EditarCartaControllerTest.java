package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.cartas.Carta;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.FileNotFoundException;
import java.util.Date;

import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class EditarCartaControllerTest {

    private static final Date HORARIO = new Date();

    private static final Metadados METADADOS = new Metadados()
            .withId("um-id-qualquer")
            .withRevisao("1234567890abcdef1234567890abcdef")
            .withAutor("Fulano de Tal")
            .withHorario(HORARIO);

    @Mock
    Carta carta;

    EditarCartaController controller;

    @Before
    public void setUp() throws Exception {
        controller = new EditarCartaController();
    }

    @Test
    public void adicionaHeadersDosMetadados() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        given(carta.getMetadados())
                .willReturn(METADADOS);

        controller.editar(carta, response);

        assertThat(response.getHeader("X-Git-Revision"), is("1234567890abcdef1234567890abcdef"));
        assertThat(response.getHeader("X-Git-Author"), is("Fulano de Tal"));
        assertThat(response.getHeader("Last-Modified"), is(valueOf(HORARIO.getTime())));
    }

    @Test
    public void retornaConteudoDoServico() throws Exception {
        given(carta.getMetadados())
                .willReturn(METADADOS);

        given(carta.getConteudo())
                .willReturn("<servico/>");

        String conteudo = controller.editar(carta, new MockHttpServletResponse());
        assertThat(conteudo, is("<servico/>"));
    }

    @Test(expected = FileNotFoundException.class)
    public void retorna404QuandoArquivoNaoEncontrado() throws Exception {
        given(carta.getMetadados())
                .willReturn(METADADOS);

        given(carta.getConteudo())
                .willThrow(new FileNotFoundException());

        controller.editar(carta, new MockHttpServletResponse());
    }
}