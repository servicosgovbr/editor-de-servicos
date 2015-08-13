package br.gov.servicos.editor.utils;

import lombok.SneakyThrows;

public abstract class Unchecked {

    /**
     * Retorna uma versão @SneakyThrows da função passada
     */
    public interface Function<T, R> {
        @SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef"})
        static <T, R> java.util.function.Function<T, R> unchecked(Unchecked.Function<T, R> delegate) {
            return new java.util.function.Function<T, R>() {
                @Override
                @SneakyThrows
                public R apply(T t) {
                    return delegate.apply(t);
                }
            };
        }

        R apply(T t) throws Exception;
    }

    /**
     * Retorna uma versão @SneakyThrows do supplier passado
     */
    public interface Supplier<T> {
        @SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef"})
        static <T> java.util.function.Supplier<T> unchecked(Unchecked.Supplier<T> delegate) {
            return new java.util.function.Supplier<T>() {
                @Override
                @SneakyThrows
                public T get() {
                    return delegate.get();
                }
            };
        }

        T get() throws Exception;
    }
}
