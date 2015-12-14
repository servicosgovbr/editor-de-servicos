package br.gov.servicos.editor.security;

import br.gov.servicos.editor.conteudo.TipoPagina;
import org.junit.Test;

import static br.gov.servicos.editor.security.TipoPermissao.CADASTRAR;
import static br.gov.servicos.editor.security.TipoPermissao.PUBLICAR;
import static br.gov.servicos.editor.security.TipoPermissao.EDITAR_SALVAR;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

public class TipoPermissaoTest {

    @Test
    public void todosNomesDevemEstarEmLetraMaiusculaEPodemConterUnderscore() {
        for (TipoPermissao tipoPermissao : TipoPermissao.values()) {
            assertTrue("Nome deve estar em letras maiusculas: " + tipoPermissao.getNome(),
                    tipoPermissao.getNome().matches("[A-Z\\s\\(\\)_]+"));
        }
    }

    @Test
    public void deveRetornarNomeDePermissaoComTipoDePaginaeEOrgaoEspecifico() {
        assertThat(PUBLICAR.comTipoPaginaParaOrgaoEspecifico(TipoPagina.ORGAO), equalTo(PUBLICAR.getNome() + " " + TipoPagina.ORGAO.getNome().toUpperCase() + " (ORGAO ESPECIFICO)"));
    }

    @Test
    public void deveRetornarNomeDePermissaoComPapel() {
        assertThat(CADASTRAR.comPapel("BLA"), equalTo(CADASTRAR.getNome() + " BLA"));
    }


    @Test
    public void deveRetornarNomeDePermissaoComTipoPagina() {
        assertThat(EDITAR_SALVAR.comTipoPagina(TipoPagina.PAGINA_TEMATICA), equalTo(EDITAR_SALVAR.getNome() + " " + TipoPagina.PAGINA_TEMATICA.getNome().toUpperCase()));
    }

}