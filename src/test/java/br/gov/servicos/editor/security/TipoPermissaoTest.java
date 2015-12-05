package br.gov.servicos.editor.security;

import org.junit.Test;

import static br.gov.servicos.editor.security.TipoPermissao.CADASTRAR;
import static br.gov.servicos.editor.security.TipoPermissao.PUBLICAR;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

public class TipoPermissaoTest {

    @Test
    public void todosNomesDevemEstarEmLetraMaiuscula() {
        for (TipoPermissao tipoPermissao : TipoPermissao.values()) {
            assertTrue("Nome deve estar em letras maiusculas: " + tipoPermissao.getNome(),
                    tipoPermissao.getNome().matches("[A-Z\\s\\(\\)]+"));
        }
    }

    @Test
    public void deveRetornarNomeDePermissaoComOrgaoEspecifico() {
        assertThat(PUBLICAR.comOrgaoEspecifico(), equalTo(PUBLICAR.getNome() + " (ORGAO ESPECIFICO)"));
    }

    @Test
    public void deveRetornarNomeDePermissaoComPapel() {
        assertThat(CADASTRAR.comPapel("BLA"), equalTo(CADASTRAR.getNome() + " BLA"));
    }

}