package br.gov.servicos.editor.security;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TipoPermissaoTest {

    @Test
    public void todosNomesDevemEstarEmLetraMaiuscula() {
        for (TipoPermissao tipoPermissao : TipoPermissao.values()) {
            assertTrue("Nome deve estar em letras maiusculas: " + tipoPermissao.getNome(), tipoPermissao.getNome().matches("[A-Z\\s]+"));
        }
    }

}