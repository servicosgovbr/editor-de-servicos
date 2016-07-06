package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.conteudo.cartas.RepositorioGitIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class OrgaosIntegrationTest extends RepositorioGitIntegrationTest {

    @Autowired
    Orgaos orgaos;

    @Before
    public void setup() {
        setupBase().build();
    }

    @Test
    public void deveListarOrgaosContendoTermoComNomeParcial() throws Exception {
        assertThat(orgaos.get("ministerio do planej"), is(asList(
                new OrgaoDTO("http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/2981",
                        "Ministério do Planejamento, Desenvolvimento e Gestão (MP)"),
                new OrgaoDTO("http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/202328",
                        "Superintendência de Administração do Ministério do Planejamento, Orçamento e Gestão nos Estado de Rondônia (SAMPRO)"),
                new OrgaoDTO("http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/202330",
                        "Superintendência de Administração do Ministério do Planejamento, Orçamento e Gestão nos Estado de Roraima (SAMPRR)"),
                new OrgaoDTO("http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/202332",
                        "Superintendência de Administração do Ministério do Planejamento, Orçamento e Gestão nos Estado do Acre (SAMPAC)"),
                new OrgaoDTO("http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/202334",
                        "Superintendência de Administração do Ministério do Planejamento, Orçamento e Gestão nos Estado do Amapá (SAMPAP)")
        )));
    }

    @Test
    public void deveListarOrgaosContendoTermoComSigla() throws Exception {
        assertThat(orgaos.get("minc"), is(asList(
                new OrgaoDTO("http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/107376",
                        "Comissão Permanente de Avaliação de Documentos Sigilosos (CPADS/MinC)"),
                new OrgaoDTO("http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/88765",
                        "Corregedoria Setorial das Áreas de Cultura e de Esporte (CORAS/MINC)"),
                new OrgaoDTO("http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/1926",
                        "Ministério da Cultura (MinC)"),
                new OrgaoDTO("http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/119453",
                        "Subsecretaria de Planejamento, Orçamento e Administração (SPOA-MinC)")
        )));
    }
}
