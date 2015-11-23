package br.gov.servicos.editor.conteudo.cartas;

import org.junit.Before;
import org.junit.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EditarPaginaControllerIntegrationTest extends RepositorioGitIntegrationTest {

    @Before
    public void setup() {
        setupBase()
                .carta("teste-a", "<servico><nome>Teste A</nome></servico>")
                .build();
    }

    @Test
    public void editar() throws Exception {
        api.editarCarta("teste-a")
                .andExpect(status().isOk())
                .andExpect(content().string("<servico><nome>Teste A</nome></servico>"));
    }

    @Test
    public void editarNovo() throws Exception {
        api.editarNovaCarta()
                .andExpect(status().isOk())
                .andExpect(content().string("<servico/>"));
    }

    @Test
    public void editarServicoNaoExistenteDeveDeixarORepositorioEmEstadoLimpo() throws Exception {
        api.editarCarta("servico-que-nao-existe")
                .andExpect(status().isNotFound());

        api.editarCarta("teste-a")
                .andExpect(status().isOk())
                .andExpect(content().string("<servico><nome>Teste A</nome></servico>"));
    }

}
