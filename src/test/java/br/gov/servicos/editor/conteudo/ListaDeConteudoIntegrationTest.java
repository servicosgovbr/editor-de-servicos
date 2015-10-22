package br.gov.servicos.editor.conteudo;

import br.gov.servicos.editor.Main;
import br.gov.servicos.editor.fixtures.MockMvcFactory;
import br.gov.servicos.editor.fixtures.RepositorioCartasBuilder;
import br.gov.servicos.editor.fixtures.RepositorioConfigParaTeste;
import br.gov.servicos.editor.git.Importador;
import br.gov.servicos.editor.git.RepositorioConfig;
import junit.framework.TestCase;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
public class ListaDeConteudoIntegrationTest extends TestCase {

    @Autowired
    WebApplicationContext context;

    @Autowired
    public RepositorioConfigParaTeste repo;

    @Autowired
    RepositorioConfig repoConfig;

    @Autowired
    Importador importador;

    MockMvcEditorAPI api;

    @Before
    public void setup() {
        api = MockMvcFactory.editorAPI(context);
        repo.reset();
        importador.importaRepositorioDeCartas();

        new RepositorioCartasBuilder(repoConfig.localRepositorioDeCartas.toPath())
                .carta("testes", "<servico><nome>testes</nome></servico>")
                .build();
    }

    @Test
    public void deveListarConteudoAposUsarPrefixoEmRenomeacao() throws Exception {
        listaDeveTerServico("testes", 1);
        renomear("testes", "servico-testes");
        listaDeveTerServico("servico-testes", 1);
    }

    public void listaDeveTerServico(String id, int size) throws Exception {
        api.listar()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(size)))
                .andExpect(jsonPath("$[0].id").value(is(id)))
                .andExpect(jsonPath("$[0].conteudo.nome").value(is(id)));
    }

    public void renomear(String de, String para) throws Exception {
        api.renomearCarta(de, para)
                .andExpect(status().isOk());
    }

}