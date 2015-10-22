package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.Main;
import br.gov.servicos.editor.conteudo.MockMvcEditorAPI;
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
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@IntegrationTest({
        "flags.importar=false",
        "flags.esquentar.cache=false",
        "flags.git.push=true",
        "server.port:0"
})
@ActiveProfiles("teste")
public class PublicarCartaControllerIntegrationTest {


    @Autowired
    WebApplicationContext context;

    @Autowired
    public RepositorioConfigParaTeste repo;

    @Autowired
    RepositorioConfig repoConfig;

    @Autowired
    Importador importador;

    private MockMvcEditorAPI api;


    @Before
    public void setup() {
        api = MockMvcFactory.editorAPI(context);

        repo.reset();
        importador.importaRepositorioDeCartas();

        new RepositorioCartasBuilder(repoConfig.localRepositorioDeCartas.toPath())
                .carta("teste-a", "<servico><nome>Teste A</nome><sigla>TSTA</sigla></servico>")
                .build();
    }

    @Test
    public void renomearAlterarPublicarNaoDeveDarConflitos() throws Exception {

        api.editarCarta("teste-a")
                .andExpect(status().isOk());

        api.salvarCarta("teste-a", "<servico><nome>Teste A</nome><sigla>TSTA</sigla><nomes-populares><item>C</item></nomes-populares></servico>")
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/editar/api/pagina/servico/teste-a"));

        api.renomearCarta("teste-a", "teste-b")
                .andExpect(status().isOk());

        api.editarCarta("teste-b")
                .andExpect(status().isOk());

        api.salvarCarta("teste-b", "<servico><nome>Teste B</nome><sigla>TSTB</sigla><nomes-populares><item>A</item><item>B</item></nomes-populares></servico>")
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/editar/api/pagina/servico/teste-b"));

        api.listar()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].temAlteracoesNaoPublicadas").value(true));

        api.publicarCarta("teste-b")
                .andExpect(status().isOk());

        api.listar()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].temAlteracoesNaoPublicadas").value(false));

        api.editarCarta("teste-b")
                .andExpect(status().isOk())
                .andExpect(content().xml("<servico><nome>Teste B</nome><sigla>TSTB</sigla><nomes-populares><item>A</item><item>B</item></nomes-populares></servico>"));

    }

}
