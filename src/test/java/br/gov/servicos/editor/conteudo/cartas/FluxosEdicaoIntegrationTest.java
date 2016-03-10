package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.config.SlugifyConfig;
import br.gov.servicos.editor.conteudo.TipoPagina;
import br.gov.servicos.editor.utils.Unchecked;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static br.gov.servicos.editor.conteudo.TipoPagina.SERVICO;
import static java.util.stream.Collectors.joining;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class FluxosEdicaoIntegrationTest extends RepositorioGitIntegrationTest {

    ExecutorService execucaoParalela;

    @Before
    public void setup() {
        execucaoParalela = Executors.newCachedThreadPool();
        setupBase()
                .carta(id("a"), carta1("A"))
                .carta(id("b"), carta1("B"))
                .carta(id("c"), carta1("C"))
                .carta(id("d"), carta1("D"))
                .carta(id("e"), carta1("E"))
                .build();
    }

    @After
    public void tearDown() {
        execucaoParalela.shutdown();
    }

    @Test
    public void equipesListandoEditandoSalvandoAoMesmoTempoDeveManterRepositorioFuncional() throws Exception {
        paralelizar(
                () -> fluxoListarSalvarEditar(SERVICO, id("a"), carta1("A"), carta2("A")),
                () -> fluxoListarSalvarEditar(SERVICO, id("b"), carta1("B"), carta2("B")),
                () -> fluxoListarSalvarEditar(SERVICO, id("c"), carta1("C"), carta2("C")));
    }

    @Test
    public void fluxoDelecaoMesmoServicoNaoDeveDarErros() throws Exception {
        paralelizar(
                () -> api.excluirPagina(SERVICO, id("a")),
                () -> api.excluirPagina(SERVICO, id("a")),
                () -> api.excluirPagina(SERVICO, id("a")),
                () -> api.excluirPagina(SERVICO, id("a")),
                () -> api.excluirPagina(SERVICO, id("a")),
                () -> api.excluirPagina(SERVICO, id("a")),
                () -> api.excluirPagina(SERVICO, id("a")),
                () -> api.excluirPagina(SERVICO, id("a")),
                () -> api.excluirPagina(SERVICO, id("a")),
                () -> api.excluirPagina(SERVICO, id("a")));

        api.editarCarta(id("a"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void fluxoDeletarPaginaRepositorioNaoDeveFicarInconsistente() {
        try {
            paralelizar(
                    () -> api.excluirPagina(SERVICO, id("a")).andExpect(status().isOk()),
                    () -> api.excluirPagina(SERVICO, id("b")).andExpect(status().isOk()),
                    () -> api.excluirPagina(SERVICO, id("c")).andExpect(status().isOk()));

            paralelizar(
                    () -> api.excluirPagina(SERVICO, id("a")).andExpect(status().isNotFound()),
                    () -> api.excluirPagina(SERVICO, id("b")).andExpect(status().isNotFound()),
                    () -> api.excluirPagina(SERVICO, id("c")).andExpect(status().isNotFound()),
                    () -> api.excluirPagina(SERVICO, id("a")).andExpect(status().isNotFound()),
                    () -> api.excluirPagina(SERVICO, id("b")).andExpect(status().isNotFound()),
                    () -> api.excluirPagina(SERVICO, id("c")).andExpect(status().isNotFound()),
                    () -> api.excluirPagina(SERVICO, id("a")).andExpect(status().isNotFound()),
                    () -> api.excluirPagina(SERVICO, id("b")).andExpect(status().isNotFound()),
                    () -> api.excluirPagina(SERVICO, id("c")).andExpect(status().isNotFound()),
                    () -> api.excluirPagina(SERVICO, id("a")).andExpect(status().isNotFound()),
                    () -> api.excluirPagina(SERVICO, id("b")).andExpect(status().isNotFound()),
                    () -> api.excluirPagina(SERVICO, id("c")).andExpect(status().isNotFound()),
                    () -> api.excluirPagina(SERVICO, id("a")).andExpect(status().isNotFound()),
                    () -> api.excluirPagina(SERVICO, id("b")).andExpect(status().isNotFound()),
                    () -> api.excluirPagina(SERVICO, id("c")).andExpect(status().isNotFound()));
        } finally {
            execucaoParalela.shutdown();
        }
    }

    @Test
    public void edicaoConcorrenteDoMesmoDocumentoDeveManterAUltimaVersaoSemErros() throws Exception {
        paralelizar(
                () -> salvarDocumentos(SERVICO, id("a"), carta1("A"), carta2("A")),
                () -> salvarDocumentos(SERVICO, id("a"), carta1("A"), carta2("A")),
                () -> salvarDocumentos(SERVICO, id("a"), carta1("A"), carta2("A")),
                () -> salvarDocumentos(SERVICO, id("a"), carta1("A"), carta2("A")),
                () -> salvarDocumentos(SERVICO, id("a"), carta1("A"), carta2("A")),
                () -> salvarDocumentos(SERVICO, id("a"), carta1("A"), carta2("A")),
                () -> salvarDocumentos(SERVICO, id("a"), carta1("A"), carta2("A")),
                () -> salvarDocumentos(SERVICO, id("a"), carta1("A"), carta2("A")));

        String ultimo = "<servico><nome>Carta A</nome><descricao>Uma descricao</descricao></servico>";

        api.salvarCarta(id("a"), ultimo)
                .andExpect(status().is3xxRedirection());

        api.editarCarta(id("a"))
                .andExpect(status().isOk())
                .andExpect(content().xml(ultimo));
    }

    @Test
    public void equipesEditandoAoMesmoTempoDeveManterRepositorioFuncional() {
        paralelizar(
                () -> fluxoListarExcluirRenomearSalvarSalvarPublicar("a", "b", "c", "d", "e"),
                () -> fluxoListarEditarSalvarPublicarDescartarDespublicarListar("z"),
                () -> fluxoListarExcluirRenomearSalvarSalvarPublicar("f", "g", "h", "i", "j"),
                () -> fluxoListarEditarSalvarPublicarDescartarDespublicarListar("x"),
                () -> fluxoListarExcluirRenomearSalvarSalvarPublicar("k", "l", "m", "n", "o"),
                () -> fluxoListarEditarSalvarPublicarDescartarDespublicarListar("v"),
                () -> fluxoListarExcluirRenomearSalvarSalvarPublicar("p", "q", "r", "s", "t"),
                () -> fluxoListarEditarSalvarPublicarDescartarDespublicarListar("1"),
                () -> fluxoListarExcluirRenomearSalvarSalvarPublicar("2", "3", "4", "5", "6"),
                () -> fluxoListarEditarSalvarPublicarDescartarDespublicarListar("y"));
    }

    @SneakyThrows
    private void fluxoListarExcluirRenomearSalvarSalvarPublicar(String id1, String id2, String id3, String id4, String id5) {
        String id3r = id3 + " r";
        String id4r = id4 + " r";
        String id5r = id5 + " r";

        api.listar()
                .andExpect(status().isOk());

        api.salvarCarta(id(id1), carta1(id1));
        api.salvarCarta(id(id2), carta1(id2));
        api.salvarCarta(id(id3), carta1(id3));
        api.salvarCarta(id(id4), carta1(id4));
        api.salvarCarta(id(id5), carta1(id5));

        api.excluirPagina(SERVICO, id(id1))
                .andExpect(status().isOk());
        api.excluirPagina(SERVICO, id(id2))
                .andExpect(status().isOk());
        api.renomearCarta(id(id3), id(id3r))
                .andExpect(status().isOk());
        api.renomearCarta(id(id4), id(id4r))
                .andExpect(status().isOk());
        api.renomearCarta(id(id5), id(id5r))
                .andExpect(status().isOk());

        api.salvarCarta(id(id1), carta1(id1))
                .andExpect(status().is3xxRedirection());
        api.salvarCarta(id(id2), carta1(id2))
                .andExpect(status().is3xxRedirection());
        api.salvarCarta(id(id3r), carta2(id3r))
                .andExpect(status().is3xxRedirection());
        api.salvarCarta(id(id4r), carta2(id4r))
                .andExpect(status().is3xxRedirection());
        api.salvarCarta(id(id5r), carta2(id5r))
                .andExpect(status().is3xxRedirection());

        api.publicarCarta(id(id1))
                .andExpect(status().isOk());
        api.publicarCarta(id(id2))
                .andExpect(status().isOk());
        api.publicarCarta(id(id3r))
                .andExpect(status().isOk());
        api.publicarCarta(id(id4r))
                .andExpect(status().isOk());
        api.publicarCarta(id(id4r))
                .andExpect(status().isOk());

        api.listar()
                .andExpect(jsonPath("$[*].id")
                        .value(Matchers.hasItems(id(id1), id(id2), id(id3r), id(id4r), id(id5r))));
    }

    @SneakyThrows
    private void fluxoListarEditarSalvarPublicarDescartarDespublicarListar(String id) {
        id = id(id);

        api.listar()
                .andExpect(status().isOk());
        api.salvarCarta(id, carta2(id))
                .andExpect(status().is3xxRedirection());
        api.publicarCarta(id)
                .andExpect(status().isOk());
        api.salvarCarta(id, carta3(id))
                .andExpect(status().is3xxRedirection());
        api.descartarCarta(id)
                .andExpect(status().isOk());
        api.editarCarta(id)
                .andExpect(status().isOk())
                .andExpect(content().xml(carta2(id)));
        api.salvarCarta(id, carta4(id))
                .andExpect(status().is3xxRedirection());
        api.publicarCarta(id)
                .andExpect(status().isOk());
        api.despublicarCarta(id)
                .andExpect(status().isOk());
        api.listar()
                .andExpect(status().isOk());
        api.editarCarta(id)
                .andExpect(status().isOk())
                .andExpect(content().xml(carta4(id)));
    }

    @SneakyThrows
    private void listar() {
        api.listar()
                .andExpect(status().isOk());
    }

    @SneakyThrows
    private void salvarDocumentos(TipoPagina tipo, String id, String conteudo1, String conteudo2) {
        api.salvarPagina(tipo, id, conteudo1)
                .andExpect(status().is3xxRedirection());
        api.salvarPagina(tipo, id, conteudo2)
                .andExpect(status().is3xxRedirection());
    }

    @SneakyThrows
    private void fluxoListarSalvarEditar(TipoPagina tipo, String id, String conteudo1, String conteudo2) {
        for (int i = 0; i < 10; i++) {
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
                .andExpect(status().isOk());
        api.listar()
                .andExpect(status().isOk());
        api.editarPagina(tipo, id)
                .andExpect(status().isOk())
                .andExpect(content().xml(conteudo1));
        api.listar()
                .andExpect(status().isOk());
    }

    @SafeVarargs
    private final <T> void paralelizar(Unchecked.Supplier<T>... fs) {
        Arrays.asList(fs)
                .stream()
                .map(f -> execucaoParalela.submit((Callable<Object>) f::get))
                .forEach(FluxosEdicaoIntegrationTest::get);
    }

    private void paralelizar(Runnable... fs) {
        Arrays.asList(fs)
                .stream()
                .map(f -> execucaoParalela.submit(f::run))
                .forEach(FluxosEdicaoIntegrationTest::get);
    }

    private static <T> Object get(Future<T> f) {
        try {
            return f.get();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private static String carta1(String base) {
        return "<servico><nome>Carta " + base + "</nome></servico>";
    }

    private static String carta2(String base) {
        return "<servico><nome>Carta " + base + "</nome><sigla>C" + base.charAt(0) + "</sigla></servico>";
    }

    private static String carta3(String base) {
        return "<servico><nome>Carta " + base + "</nome><sigla>C" + base.charAt(0) + "</sigla><descricao>" + base + "</descricao></servico>";
    }

    private static String carta4(String base) {
        String palavrasChave = "<palavras-chave>" + Arrays.asList(base.split(""))
                .stream()
                .map(s -> "<item>" + s + "</item>")
                .collect(joining("")) + "</palavras-chave>";

        return "<servico><nome>Carta " + base + "</nome><sigla>C" + base.charAt(0) + "</sigla>" + palavrasChave + "</servico>";
    }

    private static String id(String base) {
        return SlugifyConfig.slugify("carta1-" + base);
    }

}
