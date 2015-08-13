package br.gov.servicos.editor.utils;

import org.junit.Test;

public class UncheckedTest {

    @Test(expected = Exception.class)
    public void retornaVersaoSemExcecoesDaFuncao() throws Exception {
        Unchecked.Function.<Integer, Integer>unchecked(x -> {
            throw new Exception("");
        }).apply(1);
    }

    @Test(expected = Exception.class)
    public void retornaVersaoSemExcecoesDeSupplier() throws Exception {
        Unchecked.Supplier.<Integer>unchecked(() -> {
            throw new Exception("");
        }).get();
    }

}