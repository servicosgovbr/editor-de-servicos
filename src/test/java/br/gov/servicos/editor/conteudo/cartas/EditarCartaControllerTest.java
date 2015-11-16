package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.paginas.ConteudoVersionadoFactory;
import br.gov.servicos.editor.git.Metadados;
import br.gov.servicos.editor.git.Revisao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;

import java.util.Date;

import static br.gov.servicos.editor.conteudo.paginas.TipoPagina.SERVICO;
import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class EditarCartaControllerTest {

    static final Date HORARIO = new Date();

    static final Revisao REVISAO = new Revisao().withHash("da39a3ee5e6b4b0d3255bfef95601890afd80709")
            .withAutor("Fulano de Tal")
            .withHorario(HORARIO);

    static final Metadados<Carta.Servico> METADADOS = new Metadados<Carta.Servico>()
            .withEditado(REVISAO)
            .withPublicado(REVISAO);

    @Mock
    ConteudoVersionadoFactory factory;

    @Mock
    Carta carta;

    EditarCartaController controller;

    @Before
    public void setUp() throws Exception {
        given(carta.getMetadados())
                .willReturn(METADADOS);

        given(carta.getConteudoRaw())
                .willReturn("<servico/>");
        given(carta.existe())
                .willReturn(true);

        given(factory.pagina(anyString(), eq(SERVICO)))
                .willReturn(carta);

        controller = new EditarCartaController(factory);
    }

    @Test
    public void adicionaHeadersDosMetadados() throws Exception {

        HttpHeaders response = controller.editar("").getHeaders();

        assertThat(response.get("X-Git-Commit-Publicado").get(0), is("da39a3ee5e6b4b0d3255bfef95601890afd80709"));
        assertThat(response.get("X-Git-Autor-Publicado").get(0), is("Fulano de Tal"));
        assertThat(response.get("X-Git-Horario-Publicado").get(0), is(valueOf(HORARIO.getTime())));

        assertThat(response.get("X-Git-Commit-Editado").get(0), is("da39a3ee5e6b4b0d3255bfef95601890afd80709"));
        assertThat(response.get("X-Git-Autor-Editado").get(0), is("Fulano de Tal"));
        assertThat(response.get("X-Git-Horario-Editado").get(0), is(valueOf(HORARIO.getTime())));
    }

}