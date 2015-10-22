package br.gov.servicos.editor.conteudo.paginas;

import br.gov.servicos.editor.Main;
import br.gov.servicos.editor.fixtures.MockMvcFactory;
import br.gov.servicos.editor.fixtures.RepositorioCartasBuilder;
import br.gov.servicos.editor.fixtures.RepositorioConfigParaTeste;
import br.gov.servicos.editor.git.Importador;
import br.gov.servicos.editor.git.RepositorioConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static br.gov.servicos.editor.conteudo.paginas.TipoPagina.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.ALL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@IntegrationTest({
        "flags.importar=false",
        "flags.esquentar.cache=false",
        "server.port:0"
})
@ActiveProfiles("teste")
public class EditarPaginaControllerIntegrationTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    public RepositorioConfigParaTeste repo;

    @Autowired
    RepositorioConfig repoConfig;

    @Autowired
    Importador importador;

    MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcFactory.get(context);

        repo.reset();
        importador.importaRepositorioDeCartas();

        new RepositorioCartasBuilder(repoConfig.localRepositorioDeCartas.toPath())
                .paginaEspecial("pagina-a", new Pagina().withNome("Pagina a"))
                .orgao("orgao-a", new Pagina().withNome("Orgao a"))
                .areaDeInteresse("area-a", new Pagina().withNome("Area a"))
                .build();
    }

    @Test
    public void editarPaginaEspecial() throws Exception {
        editarPagina("pagina-a", PAGINA_ESPECIAL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo", is("pagina-especial")))
                .andExpect(jsonPath("$.nome", is("Pagina a")));
    }

    @Test
    public void editarAreaDeInteresse() throws Exception {
        editarPagina("area-a", AREA_DE_INTERESSE)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo", is("area-de-interesse")))
                .andExpect(jsonPath("$.nome", is("Area a")));
    }

    @Test
    public void editarOrgao() throws Exception {
        editarPagina("orgao-a", ORGAO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo", is("orgao")))
                .andExpect(jsonPath("$.nome", is("Orgao a")));
    }

    @Test
    public void editarNovo() throws Exception {
        editarPaginaNova(PAGINA_ESPECIAL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo", is("pagina-especial")));
    }

    @Test
    public void editarServicoNaoExistenteDeveDeixarORepositorioEmEstadoLimpo() throws Exception {
        editarPagina("pagina-que-nao-existe", ORGAO)
                .andExpect(status().isNotFound());

        editarPagina("orgao-a", ORGAO)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo", is("orgao")))
                .andExpect(jsonPath("$.nome", is("Orgao a")));
    }

    private ResultActions editarPagina(String id, TipoPagina tipo) throws Exception {
        return mvc.perform(get("/editar/api/pagina/" + tipo.getNome() + "/" + id).accept(ALL));
    }

    private ResultActions editarPaginaNova(TipoPagina tipo) throws Exception {
        return mvc.perform(get("/editar/api/pagina/" + tipo.getNome() + "/novo").accept(ALL));
    }

}
