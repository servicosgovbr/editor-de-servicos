package br.gov.servicos.editor.conteudo.cartas;

import org.junit.Before;
import org.junit.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FluxosEdicaoIntegrationTest extends RepositorioGitIntegrationTest {

    private static final int OPERATION_COUNT = 37;

    static String CARTA_A = "<servico><nome>Carta A</nome></servico>";
    static String CARTA_A_ALTERACOES = "<servico><nome>Carta A</nome><sigla>CA</sigla></servico>";
    static String CARTA_B = "<servico><nome>Carta B</nome></servico>";
    static String CARTA_B_ALTERACOES = "<servico><nome>Carta B</nome><sigla>CB</sigla></servico>";

    @Before
    public void setup() {
        setupBase()
                .carta("carta-a", CARTA_A)
                .carta("carta-b", CARTA_B)
                .build();
    }

    @Test
    public void testeCargaSalvar() throws Exception {
        for (int i = 0; i < OPERATION_COUNT; i++) {
            api.salvarCarta("carta-a", CARTA_A)
                    .andExpect(status().is3xxRedirection());
            api.editarCarta("carta-a")
                    .andExpect(status().isOk())
                    .andExpect(content().xml(CARTA_A));
            api.salvarCarta("carta-a", CARTA_A_ALTERACOES)
                    .andExpect(status().is3xxRedirection());
            api.editarCarta("carta-a")
                    .andExpect(status().isOk())
                    .andExpect(content().xml(CARTA_A_ALTERACOES));
            api.salvarCarta("carta-b", CARTA_B)
                    .andExpect(status().is3xxRedirection());
            api.editarCarta("carta-b")
                    .andExpect(status().isOk())
                    .andExpect(content().xml(CARTA_B));
            api.salvarCarta("carta-b", CARTA_B_ALTERACOES)
                    .andExpect(status().is3xxRedirection());
            api.editarCarta("carta-b")
                    .andExpect(status().isOk())
                    .andExpect(content().xml(CARTA_B_ALTERACOES));
        }
        api.descartarCarta("carta-a")
                .andExpect(status().is3xxRedirection());
        api.editarCarta("carta-a")
                .andExpect(status().isOk())
                .andExpect(content().xml(CARTA_A));

        api.descartarCarta("carta-b")
                .andExpect(status().is3xxRedirection());
        api.editarCarta("carta-b")
                .andExpect(status().isOk())
                .andExpect(content().xml(CARTA_B));
    }

}
