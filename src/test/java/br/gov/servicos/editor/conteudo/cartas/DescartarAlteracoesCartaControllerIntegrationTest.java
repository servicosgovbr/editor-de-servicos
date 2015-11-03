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
public class DescartarAlteracoesCartaControllerIntegrationTest {

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
                .carta("carta-a", "<servico><nome>Carta A</nome></servico>")
                .build();
    }

    @Test
    public void descartarAlteracoesDeveVoltarParaVersaoPublicada() throws Exception {
        api.salvarCarta("carta-a", "<servico><nome>Carta A</nome><sigla>CA</sigla></servico>")
                .andExpect(status().is3xxRedirection());

        api.editarCarta("carta-a")
                .andExpect(status().isOk())
                .andExpect(content().xml("<servico><nome>Carta A</nome><sigla>CA</sigla></servico>"));

        api.descartarCarta("carta-a")
                .andExpect(status().isOk());

        api.editarCarta("carta-a")
                .andExpect(status().isOk())
                .andExpect(content().xml("<servico><nome>Carta A</nome></servico>"));
    }

    @Test
    public void descartarAposRenomearDeveVoltarParaVersaoPublicada() throws Exception {
        api.salvarCarta("carta-a", "<servico><nome>Carta A</nome><sigla>CA</sigla></servico>")
                .andExpect(status().is3xxRedirection());

        api.editarCarta("carta-a")
                .andExpect(status().isOk())
                .andExpect(content().xml("<servico><nome>Carta A</nome><sigla>CA</sigla></servico>"));

        api.renomearCarta("carta-a", "Carta B")
                .andExpect(status().isOk());

        api.editarCarta("carta-b")
                .andExpect(status().isOk())
                .andExpect(content().xml("<servico><nome>Carta B</nome><sigla>CA</sigla></servico>"));

        api.descartarCarta("carta-b")
                .andExpect(status().isOk());

        api.editarCarta("carta-b")
                .andExpect(status().isOk())
                .andExpect(content().xml("<servico><nome>Carta B</nome></servico>"));
    }

    @Test
    public void descartarAlteracoesDeNovoServicoDeletaOServico() throws Exception {
        api.salvarCarta("carta-nova", "<servico><nome>Carta Nova</nome></servico>")
                .andExpect(status().is3xxRedirection());

        api.editarCarta("carta-nova")
                .andExpect(status().isOk())
                .andExpect(content().xml("<servico> <nome>Carta Nova</nome> </servico>"));

        api.descartarCarta("carta-nova")
                .andExpect(status().isOk());

        api.editarCarta("carta-nova")
                .andExpect(status().isNotFound());
    }

    @Test
    public void descartarAlteracoesDeServicosInexistentesRetornaStatusOk() throws Exception {
        api.descartarCarta("nao-existe")
                .andExpect(status().isOk());
    }

    @Test
    public void descartarAlteracoesDeServicosSemEdicoesRetornaStatusOk() throws Exception {
        api.descartarCarta("carta-a")
                .andExpect(status().isOk());
    }
}

