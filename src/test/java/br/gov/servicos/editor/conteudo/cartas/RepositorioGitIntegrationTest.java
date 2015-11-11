package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.Main;
import br.gov.servicos.editor.conteudo.MockMvcEditorAPI;
import br.gov.servicos.editor.fixtures.MockMvcFactory;
import br.gov.servicos.editor.fixtures.RepositorioCartasBuilder;
import br.gov.servicos.editor.fixtures.RepositorioConfigParaTeste;
import br.gov.servicos.editor.git.Importador;
import br.gov.servicos.editor.git.RepositorioConfig;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@IntegrationTest({
        "flags.importar=false",
        "flags.esquentar.cache=false",
        "server.port:0"
})
@ActiveProfiles("teste")
public abstract class RepositorioGitIntegrationTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    public RepositorioConfigParaTeste repo;

    @Autowired
    RepositorioConfig repoConfig;

    @Autowired
    Importador importador;

    MockMvcEditorAPI api;

    protected RepositorioCartasBuilder setupBase() {
        api = MockMvcFactory.editorAPI(context);

        repo.reset();
        importador.importaRepositorioDeCartas();

        return new RepositorioCartasBuilder(repoConfig.localRepositorioDeCartas.toPath());
    }
}
