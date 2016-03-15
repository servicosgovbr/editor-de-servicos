package br.gov.servicos.editor.conteudo;

import br.gov.servicos.editor.frontend.Siorg;
import br.gov.servicos.editor.git.ConteudoMetadados;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static br.gov.servicos.editor.conteudo.TipoPagina.PAGINA_TEMATICA;
import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class TipoPaginaTest {

    @Mock
    Siorg siorg;


    @Test
    public void fromNomeBuscaValorCorreto() throws Exception {
        TipoPagina.fromNome("orgao");
        TipoPagina.fromNome("servico");
        TipoPagina.fromNome("pagina-tematica");
    }

    @Test
    public void deserializaServicos() throws Exception {
        given(siorg.nomeDoOrgao("http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/77"))
                .willReturn(of("Secretaria da Receita Federal do Brasil (RFB)"));

        ConteudoMetadados metadados = TipoPagina.SERVICO.metadados("<servico>" +
                "<nome>Inscrição no CPF</nome>" +
                "<orgao id=\"http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/77\"/>" +
                "</servico>", siorg);

        assertThat(metadados.getTipo(), is(TipoPagina.SERVICO.getNome()));
        assertThat(metadados.getNome(), is("Inscrição no CPF"));
        assertThat(metadados.getNomeOrgao(), is("Secretaria da Receita Federal do Brasil (RFB)"));
        assertThat(metadados.getOrgaoId(), is("http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/77"));
    }

    @Test
    public void deserializaOrgaos() throws Exception {
        ConteudoMetadados metadados = TipoPagina.ORGAO.metadados("<orgao>" +
                "<url>http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/77</url>" +
                "<nome>Secretaria da Receita Federal do Brasil (RFB)</nome>\n" +
                "</orgao>", siorg);

        assertThat(metadados.getTipo(), is(TipoPagina.ORGAO.getNome()));
        assertThat(metadados.getNome(), is("Secretaria da Receita Federal do Brasil (RFB)"));
        assertThat(metadados.getNomeOrgao(), is("Secretaria da Receita Federal do Brasil (RFB)"));
        assertThat(metadados.getOrgaoId(), is("http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/77"));
    }

    @Test
    public void deserializaPáginasTemáticas() throws Exception {
        ConteudoMetadados metadados = PAGINA_TEMATICA.metadados("<pagina-tematica>" +
                "<nome>Cadastro de Pessoas Físicas (CPF)</nome>" +
                "</pagina-tematica>", siorg);

        assertThat(metadados.getTipo(), is(PAGINA_TEMATICA.getNome()));
        assertThat(metadados.getNome(), is("Cadastro de Pessoas Físicas (CPF)"));
        assertThat(metadados.getNomeOrgao(), is(" — — "));
        assertThat(metadados.getOrgaoId(), is(""));
    }

}