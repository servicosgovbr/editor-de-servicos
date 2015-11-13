package br.gov.servicos.editor.conteudo;

import br.gov.servicos.editor.conteudo.cartas.RepositorioGitIntegrationTest;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ListaDeConteudoIntegrationTest extends RepositorioGitIntegrationTest {

    @Before
    public void setup() {
        super.setupBase()
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