package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.Main;
import br.gov.servicos.editor.fixtures.MockMvcFactory;
import br.gov.servicos.editor.fixtures.RepositorioConfigParaTeste;
import br.gov.servicos.editor.git.Importador;
import br.gov.servicos.editor.git.RepositorioConfig;
import junit.framework.TestCase;
import lombok.SneakyThrows;
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
import org.springframework.web.context.WebApplicationContext;

import static java.util.Arrays.asList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@IntegrationTest({
        "flags.importar=false",
        "flags.esquentar.cache=false",
        "server.port:0"
})
@ActiveProfiles("teste")
public class IndexControllerIntegrationTest extends TestCase {


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
    }

    @Test
    public void rotasMithrilRetornamIndex() throws Exception {
        asList("/editar",
                "/editar/erro",
                "/editar/servico/:id",
                "/editar/orgao/:id",
                "/editar/area-de-interesse/:id",
                "/editar/pagina-especial/:id",
                "/editar/pagina/nova",
                "/editar/visualizar/servico/:id")
                .stream()
                .forEach(this::testarRota);
    }

    @SneakyThrows
    private void testarRota(String rota) {
        mvc.perform(get(rota))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
}