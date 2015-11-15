package br.gov.servicos.editor.conteudo.cartas;

import org.junit.Before;
import org.junit.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PublicarCartaControllerIntegrationTest extends RepositorioGitIntegrationTest {

    @Before
    public void setup() {
        setupBase()
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
