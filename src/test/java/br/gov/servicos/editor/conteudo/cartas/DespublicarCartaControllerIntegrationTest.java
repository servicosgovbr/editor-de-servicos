package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.paginas.ConteudoVersionadoFactory;
import br.gov.servicos.editor.conteudo.paginas.TipoPagina;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static br.gov.servicos.editor.conteudo.paginas.TipoPagina.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DespublicarCartaControllerIntegrationTest extends RepositorioGitIntegrationTest {

    static String CARTA_A_SIMPLES = "<servico><nome>Carta A</nome></servico>";
    static String CARTA_A_ALTERACOES = "<servico><nome>Carta A</nome><sigla>CA</sigla></servico>";
    static String CARTA_B = "<servico><nome>Carta B</nome><sigla>CB</sigla></servico>";

    @Autowired
    public ConteudoVersionadoFactory factory;

    @Before
    public void setup() {
        super.setupBase()
                .carta("carta-b", CARTA_B)
                .build();
    }

    @Test
    public void naoPublicadoComAlteracoesNaoDeveAcontecerNada() throws Exception {
        api.salvarCarta("carta-a", CARTA_A_SIMPLES)
                .andExpect(status().is3xxRedirection());
        api.editarCarta("carta-a")
                .andExpect(status().isOk())
                .andExpect(content().xml(CARTA_A_SIMPLES));

        api.despublicarCarta("carta-a")
                .andExpect(status().isOk());
        api.editarCarta("carta-a")
                .andExpect(status().isOk())
                .andExpect(content().xml(CARTA_A_SIMPLES));

        ConteudoVersionado carta = factory.pagina("carta-a", SERVICO);
        assertTrue(carta.existeNoBranch());
        assertFalse(carta.existeNoMaster());
    }

    @Test
    public void despublicarServicoInexistenteDeveRetornar404() throws Exception {
        api.despublicarCarta("carta-a")
                .andExpect(status().isNotFound());
    }

    @Test
    public void existeNoMasterExisteNoBranchMantemVersaoDoBranch() throws Exception {
        api.salvarCarta("carta-a", CARTA_A_SIMPLES);
        api.editarCarta("carta-a")
                .andExpect(status().isOk())
                .andExpect(content().xml(CARTA_A_SIMPLES));

        api.publicarCarta("carta-a");
        api.editarCarta("carta-a")
                .andExpect(status().isOk())
                .andExpect(content().xml(CARTA_A_SIMPLES));

        api.salvarCarta("carta-a", CARTA_A_ALTERACOES);
        api.editarCarta("carta-a")
                .andExpect(status().isOk())
                .andExpect(content().xml(CARTA_A_ALTERACOES));

        api.despublicarCarta("carta-a")
                .andExpect(status().isOk());

        ConteudoVersionado carta = factory.pagina("carta-a", SERVICO);
        assertTrue(carta.existeNoBranch());
        assertFalse(carta.existeNoMaster());

        api.editarCarta("carta-a")
                .andExpect(status().isOk())
                .andExpect(content().xml(CARTA_A_ALTERACOES));
    }

    @Test
    public void existeNoMasterNaoExisteNoBranchDeveTirarDoMasterEFazerCopiaParaOBranch() throws Exception {
        ConteudoVersionado carta = factory.pagina("carta-b", SERVICO);
        assertTrue(carta.existeNoMaster());
        assertFalse(carta.existeNoBranch());

        api.despublicarCarta("carta-b")
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist("X-Git-Commit-Publicado"))
                .andExpect(header().doesNotExist("X-Git-Autor-Publicado"))
                .andExpect(header().doesNotExist("X-Git-Horario-Publicado"))
                .andExpect(header().string("X-Git-Commit-Editado", notNullValue()))
                .andExpect(header().string("X-Git-Autor-Editado", notNullValue()))
                .andExpect(header().string("X-Git-Horario-Editado", notNullValue()));

        assertTrue(carta.existeNoBranch());
        assertFalse(carta.existeNoMaster());

        api.editarCarta("carta-b")
                .andExpect(status().isOk())
                .andExpect(content().xml(CARTA_B));
    }

}