package br.gov.servicos.editor.utils;

import org.junit.Test;

import java.util.function.Function;
import java.util.function.Supplier;

public class UncheckedTest {

    @Test(expected = Exception.class)
    public void retornaVersaoSemExcecoesDaFuncao() throws Exception {
        function(Unchecked.Function.unchecked(x -> {
            throw new Exception("");
        }));
    }

    @Test(expected = Exception.class)
    public void retornaVersaoSemExcecoesDeSupplier() throws Exception {
        supplier(Unchecked.Supplier.unchecked(() -> {
            throw new Exception("");
        }));
    }

    private Integer function(Function<Integer, Integer> fn) {
        return fn.apply(1);
    }

    private Integer supplier(Supplier<Integer> fn) {
        return fn.get();
    }

}