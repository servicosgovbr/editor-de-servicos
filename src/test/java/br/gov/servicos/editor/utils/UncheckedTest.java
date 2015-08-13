package br.gov.servicos.editor.utils;

import org.junit.Test;

import java.util.function.Function;

import static br.gov.servicos.editor.utils.Unchecked.Function.unchecked;

public class UncheckedTest {

    @Test(expected = Exception.class)
    public void retornaVersaoSemExcecoesDaFuncao() throws Exception {
        x(unchecked(x -> {
            throw new Exception("");
        }));
    }

    private Integer x(Function<Integer, Integer> fn) {
        return fn.apply(1);
    }

}