package br.gov.servicos.editor.conteudo.cartas;

import org.junit.Before;
import org.junit.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RenomearCartaControllerIntegrationTest extends RepositorioGitIntegrationTest {
    @Before
    public void setUp() throws Exception {
        setupBase()
                .carta("carta-a", "<servico><url>http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/1934</url><nome>Carta A</nome></servico>")
                .build();
    }

    @Test
    public void renomearUmaCartaExistenteDeveAlterarIdENome() throws Exception {
        api.renomearCarta("carta-a", "Carta B muito lokassa")
                .andExpect(status().isOk());

        api.editarCarta("carta-b-muito-lokassa")
                .andExpect(status().isOk())
                .andExpect(content().xml("<servico><url>http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/1934</url><nome>Carta B muito lokassa</nome></servico>"));
    }
}