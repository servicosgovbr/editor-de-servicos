package br.gov.servicos.editor.conteudo.paginas;

import br.gov.servicos.editor.git.Metadados;
import br.gov.servicos.editor.git.Revisao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.FileNotFoundException;
import java.util.Date;

import static br.gov.servicos.editor.conteudo.paginas.TipoPagina.ORGAO;
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
    PaginaVersionadaFactory factory;

    EditarPaginaController controller;

    @Before
    public void setUp() throws Exception {
        given(factory.pagina(anyString(), any()))
                .willReturn(pagina);

        given(pagina.getTipo())
                .willReturn(ORGAO);


        controller = new EditarPaginaController(factory);
    }

    @Test
    public void adicionaHeadersDosMetadados() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();


        given(pagina.getMetadados())
                .willReturn(METADADOS);

        given(pagina.getConteudoRaw())
                .willReturn("Ministério\n--\n\nConteúdo");

        controller.editar("orgao", "ministerio-conteudo", response);

        assertThat(response.getHeader("X-Git-Commit-Publicado"), is("da39a3ee5e6b4b0d3255bfef95601890afd80709"));
        assertThat(response.getHeader("X-Git-Autor-Publicado"), is("Fulano de Tal"));
        assertThat(response.getHeader("X-Git-Horario-Publicado"), is(valueOf(HORARIO.getTime())));

        assertThat(response.getHeader("X-Git-Commit-Editado"), is("da39a3ee5e6b4b0d3255bfef95601890afd80709"));
        assertThat(response.getHeader("X-Git-Autor-Editado"), is("Fulano de Tal"));
        assertThat(response.getHeader("X-Git-Horario-Editado"), is(valueOf(HORARIO.getTime())));
    }

    @Test
    public void retornaConteudoDaPagina() throws Exception {
        given(pagina.getMetadados())
                .willReturn(METADADOS);

        given(pagina.getConteudoRaw())
                .willReturn("Ministério\n--\n\nConteúdo");

        String conteudo = controller.editar("orgao", "ministerio-conteudo", new MockHttpServletResponse());
        assertThat(conteudo, is("{\n  \"tipo\" : \"orgao\",\n  \"nome\" : \"Ministerio\",\n  \"conteudo\" : \"Conteúdo\"\n}"));
    }

    @Test(expected = FileNotFoundException.class)
    public void retorna404QuandoArquivoNaoEncontrado() throws Exception {
        given(pagina.getMetadados())
                .willReturn(METADADOS);
        given(pagina.getConteudoRaw())
                .willThrow(new FileNotFoundException());
        controller.editar("orgao", "", new MockHttpServletResponse());
    }

}
