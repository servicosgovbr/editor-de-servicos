package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.TipoPagina;
import lombok.SneakyThrows;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static br.gov.servicos.editor.conteudo.TipoPagina.SERVICO;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FluxosEdicaoIntegrationTest extends RepositorioGitIntegrationTest {

    private static final int OPERATION_COUNT = 37;

    static String CARTA_A = "<servico><nome>Carta A</nome></servico>";
    static String CARTA_A_ALTERACOES = "<servico><nome>Carta A</nome><sigla>CA</sigla></servico>";

    static String CARTA_B = "<servico><nome>Carta B</nome></servico>";
    static String CARTA_B_ALTERACOES = "<servico><nome>Carta B</nome><sigla>CB</sigla></servico>";

    static String CARTA_C = "<servico><nome>Carta C</nome></servico>";
    static String CARTA_C_ALTERACOES = "<servico><nome>Carta C</nome><sigla>CC</sigla></servico>";

    ExecutorService exec;

    @Before
    public void setup() {
        exec = Executors.newCachedThreadPool();

        setupBase()
                .carta("carta-a", CARTA_A)
                .carta("carta-b", CARTA_B)
                .carta("carta-c", CARTA_C)
                .build();
    }

    @After
    public void tearDown() {
        exec.shutdown();
    }

    @Test
    public void testeConcorrencia() throws Exception {
        getAll(
                exec.submit(() -> fluxoListarSalvarEditar(SERVICO, "carta-a", CARTA_A, CARTA_A_ALTERACOES)),
                exec.submit(() -> fluxoListarSalvarEditar(SERVICO, "carta-b", CARTA_B, CARTA_B_ALTERACOES)),
                exec.submit(() -> fluxoListarSalvarEditar(SERVICO, "carta-c", CARTA_C, CARTA_C_ALTERACOES)));
    }

    @SneakyThrows
    private void fluxoListarSalvarEditar(TipoPagina tipo, String id, String conteudo1, String conteudo2) {
        for (int i = 0; i < OPERATION_COUNT; i++) {
            api.listar()
                    .andExpect(status().isOk());
            api.salvarPagina(tipo, id, conteudo1)
                    .andExpect(status().is3xxRedirection());
            api.listar()
                    .andExpect(status().isOk());
            api.editarPagina(tipo, id)
                    .andExpect(status().isOk())
                    .andExpect(content().xml(conteudo1));
            api.listar()
                    .andExpect(status().isOk());
            api.salvarPagina(tipo, id, conteudo2)
                    .andExpect(status().is3xxRedirection());
            api.listar()
                    .andExpect(status().isOk());
            api.editarPagina(tipo, id)
                    .andExpect(status().isOk())
                    .andExpect(content().xml(conteudo2));
            api.listar()
                    .andExpect(status().isOk());
        }
        api.descartarPagina(tipo, id)
                .andExpect(status().is3xxRedirection());
        api.listar()
                .andExpect(status().isOk());
        api.editarPagina(tipo, id)
                .andExpect(status().isOk())
                .andExpect(content().xml(conteudo1));
        api.listar()
                .andExpect(status().isOk());
    }


    @Test
    public void fluxoDeletarPaginaRepositorioNaoDeveFicarInconsistente() {
        try {
            getAll(
                    exec.submit(() -> api.excluirPagina(TipoPagina.SERVICO, "carta-a").andExpect(status().isOk())),
                    exec.submit(() -> api.excluirPagina(TipoPagina.SERVICO, "carta-b").andExpect(status().isOk())),
                    exec.submit(() -> api.excluirPagina(TipoPagina.SERVICO, "carta-c").andExpect(status().isOk())));

            getAll(
                    exec.submit(() -> api.excluirPagina(TipoPagina.SERVICO, "carta-a").andExpect(status().isNotFound())),
                    exec.submit(() -> api.excluirPagina(TipoPagina.SERVICO, "carta-b").andExpect(status().isNotFound())),
                    exec.submit(() -> api.excluirPagina(TipoPagina.SERVICO, "carta-c").andExpect(status().isNotFound())),
                    exec.submit(() -> api.excluirPagina(TipoPagina.SERVICO, "carta-a").andExpect(status().isNotFound())),
                    exec.submit(() -> api.excluirPagina(TipoPagina.SERVICO, "carta-b").andExpect(status().isNotFound())),
                    exec.submit(() -> api.excluirPagina(TipoPagina.SERVICO, "carta-c").andExpect(status().isNotFound())),
                    exec.submit(() -> api.excluirPagina(TipoPagina.SERVICO, "carta-a").andExpect(status().isNotFound())),
                    exec.submit(() -> api.excluirPagina(TipoPagina.SERVICO, "carta-b").andExpect(status().isNotFound())),
                    exec.submit(() -> api.excluirPagina(TipoPagina.SERVICO, "carta-c").andExpect(status().isNotFound())),
                    exec.submit(() -> api.excluirPagina(TipoPagina.SERVICO, "carta-a").andExpect(status().isNotFound())),
                    exec.submit(() -> api.excluirPagina(TipoPagina.SERVICO, "carta-b").andExpect(status().isNotFound())),
                    exec.submit(() -> api.excluirPagina(TipoPagina.SERVICO, "carta-c").andExpect(status().isNotFound())),
                    exec.submit(() -> api.excluirPagina(TipoPagina.SERVICO, "carta-a").andExpect(status().isNotFound())),
                    exec.submit(() -> api.excluirPagina(TipoPagina.SERVICO, "carta-b").andExpect(status().isNotFound())),
                    exec.submit(() -> api.excluirPagina(TipoPagina.SERVICO, "carta-c").andExpect(status().isNotFound())));
        } finally {
            exec.shutdown();
        }
    }

    private void getAll(Future... fs) {
        Arrays.asList(fs)
                .forEach(f -> {
                    try {
                        f.get();
                    } catch (Throwable t) {
                        throw new RuntimeException(t);
                    }
                });
    }

}
