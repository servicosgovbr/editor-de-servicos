package br.gov.servicos.editor.conteudo.cartas;

import org.junit.Before;
import org.junit.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DescartarAlteracoesCartaControllerIntegrationTest extends RepositorioGitIntegrationTest {

    @Before
    public void setup() {
        setupBase()
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
                .andExpect(status().is3xxRedirection());

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
                .andExpect(status().is3xxRedirection());

        api.editarCarta("carta-b")
                .andExpect(status().isOk())
                .andExpect(content().xml("<servico><nome>Carta B</nome></servico>"));
    }

    @Test
    public void descartarAlteracoesDeNovoServicoSemEstarPublicadaNadaAcontece() throws Exception {
        api.salvarCarta("carta-nova", "<servico><nome>Carta Nova</nome></servico>")
                .andExpect(status().is3xxRedirection());

        api.editarCarta("carta-nova")
                .andExpect(status().isOk())
                .andExpect(content().xml("<servico> <nome>Carta Nova</nome> </servico>"));

        api.descartarCarta("carta-nova")
                .andExpect(status().is(406));
    }

    @Test
    public void descartarAlteracoesDeServicosInexistentesRetornaStatusOk() throws Exception {
        api.descartarCarta("nao-existe")
                .andExpect(status().isNotFound());
    }

    @Test
    public void descartarAlteracoesDeServicosSemEdicoesRetornaStatusOk() throws Exception {
        api.descartarCarta("carta-a")
                .andExpect(status().is3xxRedirection());
    }
}

