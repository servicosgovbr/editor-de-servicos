package br.gov.servicos.editor.conteudo.cartas;

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

import static org.springframework.http.MediaType.ALL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
public class EditarCartaControllerIntegrationTest {

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
        mvc = MockMvcFactory.semSprinSecurity(context);

        repo.reset();
        importador.importaRepositorioDeCartas();

        new RepositorioCartasBuilder(repoConfig.localRepositorioDeCartas.toPath())
                .carta("teste-a", "<servico><nome>Teste A</nome></servico>")
                .build();
    }

    @Test
    public void editar() throws Exception {
        editarCarta("teste-a")
                .andExpect(status().isOk())
                .andExpect(content().string("<servico><nome>Teste A</nome></servico>"));
    }

    @Test
    public void editarNovo() throws Exception {
        editarNova()
                .andExpect(status().isOk())
                .andExpect(content().string("<servico/>"));
    }

    @Test
    public void editarServicoNaoExistenteDeveDeixarORepositorioEmEstadoLimpo() throws Exception {
        editarCarta("servico-que-nao-existe")
                .andExpect(status().isNotFound());

        editarCarta("teste-a")
                .andExpect(status().isOk())
                .andExpect(content().string("<servico><nome>Teste A</nome></servico>"));
    }

    private ResultActions editarCarta(String id) throws Exception {
        return mvc.perform(get("/editar/api/pagina/servico/" + id).accept(ALL));
    }

    private ResultActions editarNova() throws Exception {
        return mvc.perform(get("/editar/api/pagina/servico/novo").accept(ALL));
    }

}
