package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.Main;
import br.gov.servicos.editor.servicos.Orgao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@IntegrationTest({
        "flags.importar=false",
        "flags.esquentar.cache=false",
        "server.port:0"
})
public class OrgaosIntegrationTest {

    @Autowired
    Orgaos orgaos;

    @Test
    public void deveListarOrgaosContendoTermoComNomeParcial() throws Exception {
        assertThat(orgaos.get("ministerio do planej"), is(asList(
                new Orgao("http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/2981",
                        "Ministério do Planejamento, Orçamento e Gestão (MP)"),
                new Orgao("http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/202328",
                        "Superintendência de Administração do Ministério do Planejamento, Orçamento e Gestão nos Estado de Rondônia (SAMPRO)"),
                new Orgao("http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/202330",
                        "Superintendência de Administração do Ministério do Planejamento, Orçamento e Gestão nos Estado de Roraima (SAMPRR)"),
                new Orgao("http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/202332",
                        "Superintendência de Administração do Ministério do Planejamento, Orçamento e Gestão nos Estado do Acre (SAMPAC)"),
                new Orgao("http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/202334",
                        "Superintendência de Administração do Ministério do Planejamento, Orçamento e Gestão nos Estado do Amapá (SAMPAP)")
        )));
    }

    @Test
    public void deveListarOrgaosContendoTermoComSigla() throws Exception {
        assertThat(orgaos.get("minc"), is(asList(
                new Orgao("http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/107376",
                        "Comissão Permanente de Avaliação de Documentos Sigilosos (CPADS/MinC)"),
                new Orgao("http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/88765",
                        "Corregedoria Setorial das Áreas de Cultura e de Esporte (CORAS/MINC)"),
                new Orgao("http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/1926",
                        "Ministério da Cultura (MinC)"),
                new Orgao("http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/119453",
                        "Subsecretaria de Planejamento, Orçamento e Administração (SPOA-MinC)")
        )));
    }
}