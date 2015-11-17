package br.gov.servicos.editor.conteudo.paginas;

import br.gov.servicos.editor.conteudo.ConteudoVersionadoFactory;
import br.gov.servicos.editor.conteudo.EditarPaginaController;
import br.gov.servicos.editor.conteudo.Pagina;
import br.gov.servicos.editor.conteudo.PaginaVersionada;
import br.gov.servicos.editor.git.Metadados;
import br.gov.servicos.editor.git.Revisao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Date;

import static br.gov.servicos.editor.conteudo.TipoPagina.ORGAO;
import static java.lang.String.valueOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class EditarPaginaControllerTest {

    static final Date HORARIO = new Date();

    static final Revisao REVISAO = new Revisao().withHash("da39a3ee5e6b4b0d3255bfef95601890afd80709")
            .withAutor("Fulano de Tal")
            .withHorario(HORARIO);

    static final Metadados<Pagina> METADADOS = new Metadados<Pagina>()
            .withEditado(REVISAO)
            .withPublicado(REVISAO)
            .withConteudo(new Pagina().withNome("Ministerio").withTipo(ORGAO.getNome()));

    @Mock
    PaginaVersionada pagina;

    @Mock
    ConteudoVersionadoFactory factory;

    EditarPaginaController controller;

    @Before
    public void setUp() throws Exception {
        given(factory.pagina(anyString(), any()))
                .willReturn(pagina);

        given(pagina.existe())
                .willReturn(true);

        given(pagina.getTipo())
                .willReturn(ORGAO);


        controller = new EditarPaginaController(factory);
    }

    @Test
    public void adicionaHeadersDosMetadados() throws Exception {
        given(pagina.getMetadados())
                .willReturn(METADADOS);

        given(pagina.getConteudoRaw())
                .willReturn("Ministério\n--\n\nConteúdo");

        HttpHeaders headers = controller.editar("orgao", "ministerio-conteudo").getHeaders();

        assertThat(headers.get("X-Git-Commit-Publicado").get(0), is("da39a3ee5e6b4b0d3255bfef95601890afd80709"));
        assertThat(headers.get("X-Git-Autor-Publicado").get(0), is("Fulano de Tal"));
        assertThat(headers.get("X-Git-Horario-Publicado").get(0), is(valueOf(HORARIO.getTime())));

        assertThat(headers.get("X-Git-Commit-Editado").get(0), is("da39a3ee5e6b4b0d3255bfef95601890afd80709"));
        assertThat(headers.get("X-Git-Autor-Editado").get(0), is("Fulano de Tal"));
        assertThat(headers.get("X-Git-Horario-Editado").get(0), is(valueOf(HORARIO.getTime())));
    }

    @Test
    public void retornaConteudoDaPagina() throws Exception {
        given(pagina.getMetadados())
                .willReturn(METADADOS);

        given(pagina.getConteudoRaw())
                .willReturn("Ministério\n--\n\nConteúdo");

        String conteudo = (String) controller.editar("orgao", "ministerio-conteudo").getBody();
        assertThat(conteudo, is("{\n  \"tipo\" : \"orgao\",\n  \"nome\" : \"Ministério\",\n  \"conteudo\" : \"Conteúdo\"\n}"));
    }

}
