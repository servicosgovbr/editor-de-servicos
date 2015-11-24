package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionadoFactory;
import br.gov.servicos.editor.conteudo.TipoPagina;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static br.gov.servicos.editor.conteudo.TipoPagina.*;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExcluirPaginaControllerIntegrationTest extends RepositorioGitIntegrationTest {

    public static final String CARTA_SIMPLES = "<servico><nome>a</nome></servico>";

    public static final String PAGINA_SIMPLES = "<pagina-tematica><nome>a</nome></pagina-tematica>";

    public static final String ORGAO_SIMPLES = "<orgao><nome>a</nome></orgao>";


    @Autowired
    ConteudoVersionadoFactory factory;

    @Before
    public void setup() {
        setupBase()
                .carta("carta-a", CARTA_SIMPLES)
                .paginaTematica("pagina-a", PAGINA_SIMPLES)
                .orgao("orgao-a", ORGAO_SIMPLES)
                .build();
    }

    @Test
    public void excluirServicoExistenteNoMasterDeveRetornar200Ok() throws Exception {
        excluirPaginaExistenteNoMasterDeveRetornar200Ok(SERVICO, "carta-a");
    }

    @Test
    public void excluirPaginaTematicaExistenteNoMasterDeveRetornar200Ok() throws Exception {
        excluirPaginaExistenteNoMasterDeveRetornar200Ok(PAGINA_TEMATICA, "pagina-a");
    }

    @Test
    public void excluirOrgaoExistenteNoMasterDeveRetornar406Ok() throws Exception {
        api.excluirPagina(ORGAO, "orgao-a")
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void excluirPaginaInexistenteRetorna404() throws Exception {
        api.excluirPagina(SERVICO, "carta-b")
                .andExpect(status().isNotFound());
    }

    private void excluirPaginaExistenteNoMasterDeveRetornar200Ok(TipoPagina tipo, String id) throws Exception {
        api.excluirPagina(tipo, id)
                .andExpect(status().isOk());

        assertFalse(factory.pagina(id, tipo)
                .existe());
    }

}