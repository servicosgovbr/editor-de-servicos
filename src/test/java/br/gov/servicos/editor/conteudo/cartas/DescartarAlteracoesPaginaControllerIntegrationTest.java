package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.TipoPagina;
import org.junit.Before;
import org.junit.Test;

import static br.gov.servicos.editor.conteudo.TipoPagina.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DescartarAlteracoesPaginaControllerIntegrationTest extends RepositorioGitIntegrationTest {

    public static final String PAGINA_A = "<pagina-tematica><url>http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/1934</url><nome>Pagina A</nome></pagina-tematica>";
    public static final String PAGINA_A_2 = "<pagina-tematica><url>http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/1934</url><nome>Pagina A</nome><conteudo>conteudo</conteudo></pagina-tematica>";

    public static final String CARTA_A = "<servico><url>http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/1934</url><nome>Carta A</nome></servico>";
    public static final String CARTA_A_2 = "<servico><url>http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/1934</url><nome>Carta A</nome><sigla>CA</sigla></servico>";

    public static final String ORGAO_A = "<orgao><url>http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/1934</url><nome>Carta A</nome></orgao>";
    public static final String ORGAO_A_2 = "<orgao><url>http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/1934</url><nome>Carta A</nome><conteudo>CA</conteudo></orgao>";

    @Before
    public void setup() {
        setupBase()
                .carta("carta-a", CARTA_A)
                .paginaTematica("pagina-a", PAGINA_A)
                .orgao("orgao-a", ORGAO_A)
                .build();
    }

    @Test
    public void descartarDeServicoAlteracoesDeveVoltarParaVersaoPublicada() throws Exception {
        descartarAlteracoesDeveVoltarParaVersaoPublicada(SERVICO, "carta-a", CARTA_A, CARTA_A_2);
    }

    @Test
    public void descartarDePaginaTematicaAlteracoesDeveVoltarParaVersaoPublicada() throws Exception {
        descartarAlteracoesDeveVoltarParaVersaoPublicada(PAGINA_TEMATICA, "pagina-a", PAGINA_A, PAGINA_A_2);
    }

    @Test
    public void descartarDeOrgaoAlteracoesDeveVoltarParaVersaoPublicada() throws Exception {
        descartarAlteracoesDeveVoltarParaVersaoPublicada(ORGAO, "orgao-a", ORGAO_A, ORGAO_A_2);
    }

    private void descartarAlteracoesDeveVoltarParaVersaoPublicada(TipoPagina tipo, String id, String conteudo1, String conteudo2) throws Exception {
        api.salvarPagina(tipo, id, conteudo2)
                .andExpect(status().is3xxRedirection());

        api.editarPagina(tipo, id)
                .andExpect(status().isOk())
                .andExpect(content().xml(conteudo2));

        api.descartarPagina(tipo, id)
                .andExpect(status().isOk());

        api.editarPagina(tipo, id)
                .andExpect(status().isOk())
                .andExpect(content().xml(conteudo1));
    }

    @Test
    public void descartarAposRenomearDeveVoltarParaVersaoPublicada() throws Exception {
        api.salvarCarta("carta-a", "<servico><url>http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/1934</url><nome>Carta A</nome><sigla>CA</sigla></servico>")
                .andExpect(status().is3xxRedirection());

        api.editarCarta("carta-a")
                .andExpect(status().isOk())
                .andExpect(content().xml("<servico><url>http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/1934</url><nome>Carta A</nome><sigla>CA</sigla></servico>"));

        api.renomearCarta("carta-a", "Carta B")
                .andExpect(status().isOk());

        api.editarCarta("carta-b")
                .andExpect(status().isOk())
                .andExpect(content().xml("<servico><url>http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/1934</url><nome>Carta B</nome><sigla>CA</sigla></servico>"));

        api.descartarCarta("carta-b")
                .andExpect(status().isOk());

        api.editarCarta("carta-b")
                .andExpect(status().isOk())
                .andExpect(content().xml("<servico><url>http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/1934</url><nome>Carta B</nome></servico>"));
    }

    @Test
    public void descartarAlteracoesDeNovoServicoSemEstarPublicadaNadaAcontece() throws Exception {
        descartarAlteracoesDeDocumentoNovoSemEstarPublicadaNadaAcontece(SERVICO, "carta-nova", CARTA_A);
    }

    @Test
    public void descartarAlteracoesDeNovaPaginaTematicaSemEstarPublicadaNadaAcontece() throws Exception {
        descartarAlteracoesDeDocumentoNovoSemEstarPublicadaNadaAcontece(PAGINA_TEMATICA, "pagina-nova", PAGINA_A);
    }

    @Test
    public void descartarAlteracoesDeNovoOrgaoSemEstarPublicadaNadaAcontece() throws Exception {
        descartarAlteracoesDeDocumentoNovoSemEstarPublicadaNadaAcontece(ORGAO, "orgao-nova", ORGAO_A);
    }

    private void descartarAlteracoesDeDocumentoNovoSemEstarPublicadaNadaAcontece(TipoPagina tipo, String id, String conteudo) throws Exception {
        api.salvarPagina(tipo, id, conteudo)
                .andExpect(status().is3xxRedirection());

        api.editarPagina(tipo, id)
                .andExpect(status().isOk())
                .andExpect(content().xml(conteudo));

        api.descartarPagina(tipo, id)
                .andExpect(status().is(406));
    }

    @Test
    public void descartarAlteracoesInexistentesRetornaStatusOk() throws Exception {
        api.descartarCarta("nao-existe")
                .andExpect(status().isNotFound());
        api.descartarPagina(PAGINA_TEMATICA, "nao-existe")
                .andExpect(status().isNotFound());
        api.descartarPagina(ORGAO, "nao-existe")
                .andExpect(status().isNotFound());
    }

    @Test
    public void descartarAlteracoesDeServicosSemEdicoesRetornaStatusOk() throws Exception {
        descartarAlteracoesSemEdicoesRetornaStatusOk(SERVICO, "carta-a");
    }

    @Test
    public void descartarAlteracoesDePaginaTematicaSemEdicoesRetornaStatusOk() throws Exception {
        descartarAlteracoesSemEdicoesRetornaStatusOk(PAGINA_TEMATICA, "pagina-a");
    }

    @Test
    public void descartarAlteracoesDeOrgaoSemEdicoesRetornaStatusOk() throws Exception {
        descartarAlteracoesSemEdicoesRetornaStatusOk(ORGAO, "orgao-a");
    }

    private void descartarAlteracoesSemEdicoesRetornaStatusOk(TipoPagina tipo, String id) throws Exception {
        api.descartarPagina(tipo, id)
                .andExpect(status().isOk());
    }

}

