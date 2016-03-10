package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.ConteudoVersionadoFactory;
import br.gov.servicos.editor.conteudo.TipoPagina;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static br.gov.servicos.editor.conteudo.TipoPagina.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DespublicarPaginaControllerIntegrationTest extends RepositorioGitIntegrationTest {

    static String CARTA_A_SIMPLES = "<servico><nome>Carta A</nome></servico>";
    static String CARTA_A_ALTERACOES = "<servico><nome>Carta A</nome><sigla>CA</sigla></servico>";
    static String CARTA_B = "<servico><nome>Carta B</nome><sigla>CB</sigla></servico>";

    static String PAGINA_A_SIMPLES = "<pagina-tematica><nome>Pagina A</nome></pagina-tematica>";
    static String PAGINA_A_ALTERACOES = "<pagina-tematica><nome>Carta A</nome><conteudo>conteudo</conteudo></pagina-tematica>";

    static String ORGAO_A_SIMPLES = "<orgao><nome>Orgao A</nome></orgao>";
    static String ORGAO_A_ALTERACOES = "<orgao><nome>Orgao A</nome><conteudo>conteudo de orgao</conteudo></orgao>";

    @Autowired
    public ConteudoVersionadoFactory factory;

    @Before
    public void setup() {
        setupBase()
                .carta("carta-b", CARTA_B)
                .paginaTematica("pagina-a", PAGINA_A_ALTERACOES)
                .orgao("orgao-a", ORGAO_A_ALTERACOES)
                .build();
    }

    @Test
    public void servicoNaoPublicadoEComAlteracoesNaoAconteceNada() throws Exception {
        docNaoPublicadoEComAlteracoesNaoAconteceNada(SERVICO, "carta-a", CARTA_A_SIMPLES);
    }

    @Test
    public void paginaNaoPublicadoEComAlteracoesNaoAconteceNada() throws Exception {
        docNaoPublicadoEComAlteracoesNaoAconteceNada(PAGINA_TEMATICA, "pagina-a", PAGINA_A_SIMPLES);
    }

    @Test
    public void orgaoNaoPublicadoEComAlteracoesNaoAconteceNada() throws Exception {
        docNaoPublicadoEComAlteracoesNaoAconteceNada(ORGAO, "orgao-a", ORGAO_A_SIMPLES);
    }

    private void docNaoPublicadoEComAlteracoesNaoAconteceNada(TipoPagina tipo, String id, String conteudo) throws Exception {
        api.salvarPagina(tipo, id, conteudo)
                .andExpect(status().is3xxRedirection());
        api.editarPagina(tipo, id)
                .andExpect(status().isOk())
                .andExpect(content().xml(conteudo));

        api.despublicarPagina(tipo, id)
                .andExpect(status().isOk());
        api.editarPagina(tipo, id)
                .andExpect(status().isOk())
                .andExpect(content().xml(conteudo));

        ConteudoVersionado doc = factory.pagina(id, tipo);
        assertTrue(doc.existeNoBranch());
        assertFalse(doc.existeNoMaster());
    }


    @Test
    public void despublicarServicoInexistenteDeveRetornar404() throws Exception {
        despublicarDocumentoInexistenteDeveRetornar404(SERVICO, "carta-a");
    }

    @Test
    public void despublicarPaginaInexistenteDeveRetornar404() throws Exception {
        despublicarDocumentoInexistenteDeveRetornar404(PAGINA_TEMATICA, "pagina-b");
    }

    @Test
    public void despublicarOrgaoInexistenteDeveRetornar404() throws Exception {
        despublicarDocumentoInexistenteDeveRetornar404(ORGAO, "orgao-b");
    }

    private void despublicarDocumentoInexistenteDeveRetornar404(TipoPagina tipo, String id) throws Exception {
        api.despublicarPagina(tipo, id)
                .andExpect(status().isNotFound());
    }

    @Test
    public void servicoExisteNoMasterExisteNoBranchMantemVersaoDoBranch() throws Exception {
        docExisteNoMasterExisteNoBranchMantemVersaoDoBranch(SERVICO, "carta-a", CARTA_A_SIMPLES, CARTA_A_ALTERACOES);
    }

    @Test
    public void paginaExisteNoMasterExisteNoBranchMantemVersaoDoBranch() throws Exception {
        docExisteNoMasterExisteNoBranchMantemVersaoDoBranch(PAGINA_TEMATICA, "pagina-a", PAGINA_A_ALTERACOES, PAGINA_A_SIMPLES);
    }

    @Test
    public void orgaoExisteNoMasterExisteNoBranchMantemVersaoDoBranch() throws Exception {
        docExisteNoMasterExisteNoBranchMantemVersaoDoBranch(ORGAO, "orgao-a", ORGAO_A_ALTERACOES, ORGAO_A_SIMPLES);
    }

    private void docExisteNoMasterExisteNoBranchMantemVersaoDoBranch(TipoPagina tipo, String id, String conteudo1, String conteudo2) throws Exception {
        api.salvarPagina(tipo, id, conteudo1);
        api.editarPagina(tipo, id)
                .andExpect(status().isOk())
                .andExpect(content().xml(conteudo1));

        api.publicarPagina(tipo, id);
        api.editarPagina(tipo, id)
                .andExpect(status().isOk())
                .andExpect(content().xml(conteudo1));

        api.salvarPagina(tipo, id, conteudo2);
        api.editarPagina(tipo, id)
                .andExpect(status().isOk())
                .andExpect(content().xml(conteudo2));

        api.despublicarPagina(tipo, id)
                .andExpect(status().isOk());

        ConteudoVersionado carta = factory.pagina(id, tipo);
        assertTrue(carta.existeNoBranch());
        assertFalse(carta.existeNoMaster());

        api.editarPagina(tipo, id)
                .andExpect(status().isOk())
                .andExpect(content().xml(conteudo2));
    }

    @Test
    public void servicoExisteNoMasterNaoExisteNoBranchDeveTirarDoMasterEFazerCopiaParaOBranch() throws Exception {
        docExisteNoMasterNaoExisteNoBranchDeveTirarDoMasterEFazerCopiaParaOBranch(SERVICO, "carta-b", CARTA_B);
    }

    @Test
    public void paginaExisteNoMasterNaoExisteNoBranchDeveTirarDoMasterEFazerCopiaParaOBranch() throws Exception {
        docExisteNoMasterNaoExisteNoBranchDeveTirarDoMasterEFazerCopiaParaOBranch(PAGINA_TEMATICA, "pagina-a", PAGINA_A_ALTERACOES);
    }

    @Test
    public void orgaoExisteNoMasterNaoExisteNoBranchDeveTirarDoMasterEFazerCopiaParaOBranch() throws Exception {
        docExisteNoMasterNaoExisteNoBranchDeveTirarDoMasterEFazerCopiaParaOBranch(ORGAO, "orgao-a", ORGAO_A_ALTERACOES);
    }

    private void docExisteNoMasterNaoExisteNoBranchDeveTirarDoMasterEFazerCopiaParaOBranch(TipoPagina tipo, String id, String conteudo) throws Exception {
        ConteudoVersionado doc = factory.pagina(id, tipo);
        assertTrue(doc.existeNoMaster());
        assertFalse(doc.existeNoBranch());

        api.despublicarPagina(tipo, id)
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist("X-Git-Commit-Publicado"))
                .andExpect(header().doesNotExist("X-Git-Autor-Publicado"))
                .andExpect(header().doesNotExist("X-Git-Horario-Publicado"))
                .andExpect(header().string("X-Git-Commit-Editado", notNullValue()))
                .andExpect(header().string("X-Git-Autor-Editado", notNullValue()))
                .andExpect(header().string("X-Git-Horario-Editado", notNullValue()));

        assertTrue(doc.existeNoBranch());
        assertFalse(doc.existeNoMaster());

        api.editarPagina(tipo, id)
                .andExpect(status().isOk())
                .andExpect(content().xml(conteudo));
    }

}