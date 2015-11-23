package br.gov.servicos.editor.conteudo.cartas;

import org.junit.Test;

import static br.gov.servicos.editor.conteudo.TipoPagina.PAGINA_TEMATICA;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PublicarPaginaControllerIntegrationTest extends RepositorioGitIntegrationTest {

    public static final String CONTEUDO_PAGINA_TEMATICA = "<pagina-tematica><nome>Pagina A</nome></pagina-tematica>";

    @Test
    public void renomearAlterarPublicarNaoDeveDarConflitos() throws Exception {
        setupBase()
                .carta("teste-a", "<servico><nome>Teste A</nome><sigla>TSTA</sigla></servico>")
                .build();

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

    @Test
    public void publicarPaginaTematica() throws Exception {
        setupBase()
                .paginaTematica("p-a", CONTEUDO_PAGINA_TEMATICA)
                .build();

        api.editarPagina(PAGINA_TEMATICA, "p-a")
                .andExpect(status().isOk())
                .andExpect(content().xml(CONTEUDO_PAGINA_TEMATICA));

        api.salvarPagina(PAGINA_TEMATICA, "p-a", "<pagina-tematica><nome>Pagina A</nome><conteudo>blah</conteudo></pagina-tematica>")
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/editar/api/pagina/pagina-tematica/p-a"));

        api.listar()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].temAlteracoesNaoPublicadas").value(true));

        api.publicarPagina(PAGINA_TEMATICA, "p-a")
                .andExpect(status().isOk());

        api.listar()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].temAlteracoesNaoPublicadas").value(false));

        api.editarPagina(PAGINA_TEMATICA, "p-a")
                .andExpect(status().isOk())
                .andExpect(content().xml("<pagina-tematica><nome>Pagina A</nome><conteudo>blah</conteudo></pagina-tematica>"));
    }
}
