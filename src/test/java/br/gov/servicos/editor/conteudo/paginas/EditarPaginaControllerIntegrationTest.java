package br.gov.servicos.editor.conteudo.paginas;

import br.gov.servicos.editor.conteudo.cartas.RepositorioGitIntegrationTest;
import org.junit.Before;
import org.junit.Test;

import static br.gov.servicos.editor.conteudo.TipoPagina.ORGAO;
import static br.gov.servicos.editor.conteudo.TipoPagina.PAGINA_TEMATICA;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EditarPaginaControllerIntegrationTest extends RepositorioGitIntegrationTest {

    @Before
    public void setup() {
        setupBase()
                .paginaTematica("pagina-a", "<pagina-tematica><nome>Pagina A</nome></pagina-tematica>")
                .orgao("orgao-a", "<orgao><nome>Orgao a</nome></orgao>")
                .build();
    }

    @Test
    public void editarPaginaTematica() throws Exception {
        api.editarPagina(PAGINA_TEMATICA, "pagina-a")
                .andExpect(status().isOk())
                .andExpect(content().xml("<pagina-tematica><nome>Pagina A</nome></pagina-tematica>"));
    }

    @Test
    public void editarOrgao() throws Exception {
        api.editarPagina(ORGAO, "orgao-a")
                .andExpect(status().isOk())
                .andExpect(content().xml("<orgao><nome>Orgao a</nome></orgao>"));
    }

    @Test
    public void editarNovo() throws Exception {
        api.editarPaginaNova(PAGINA_TEMATICA)
                .andExpect(status().isOk())
                .andExpect(content().xml("<pagina-tematica></pagina-tematica>"));
    }

    @Test
    public void editarServicoNaoExistenteDeveDeixarORepositorioEmEstadoLimpo() throws Exception {
        api.editarPagina(ORGAO, "pagina-que-nao-existe")
                .andExpect(status().isNotFound());

        api.editarPagina(ORGAO, "orgao-a")
                .andExpect(status().isOk())
                .andExpect(content().xml("<orgao><nome>Orgao a</nome></orgao>"));
    }
}
