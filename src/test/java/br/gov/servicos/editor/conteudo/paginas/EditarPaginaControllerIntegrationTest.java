package br.gov.servicos.editor.conteudo.paginas;

import br.gov.servicos.editor.conteudo.cartas.RepositorioGitIntegrationTest;
import org.junit.Before;
import org.junit.Test;

import static br.gov.servicos.editor.conteudo.paginas.TipoPagina.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EditarPaginaControllerIntegrationTest extends RepositorioGitIntegrationTest {

    @Before
    public void setup() {
        setupBase()
                .paginaEspecial("pagina-a", new Pagina().withNome("Pagina a"))
                .orgao("orgao-a", new Pagina().withNome("Orgao a"))
                .areaDeInteresse("area-a", new Pagina().withNome("Area a"))
                .build();
    }

    @Test
    public void editarPaginaEspecial() throws Exception {
        api.editarPagina("pagina-a", PAGINA_ESPECIAL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo", is("pagina-especial")))
                .andExpect(jsonPath("$.nome", is("Pagina a")));
    }

    @Test
    public void editarAreaDeInteresse() throws Exception {
        api.editarPagina("area-a", AREA_DE_INTERESSE)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo", is("area-de-interesse")))
                .andExpect(jsonPath("$.nome", is("Area a")));
    }

    @Test
    public void editarOrgao() throws Exception {
        api.editarPagina("orgao-a", ORGAO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo", is("orgao")))
                .andExpect(jsonPath("$.nome", is("Orgao a")));
    }

    @Test
    public void editarNovo() throws Exception {
        api.editarPaginaNova(PAGINA_ESPECIAL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo", is("pagina-especial")));
    }

    @Test
    public void editarServicoNaoExistenteDeveDeixarORepositorioEmEstadoLimpo() throws Exception {
        api.editarPagina("pagina-que-nao-existe", ORGAO)
                .andExpect(status().isNotFound());

        api.editarPagina("orgao-a", ORGAO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo", is("orgao")))
                .andExpect(jsonPath("$.nome", is("Orgao a")));
    }

}
