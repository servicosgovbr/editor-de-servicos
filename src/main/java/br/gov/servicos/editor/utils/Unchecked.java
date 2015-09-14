package br.gov.servicos.editor.utils;

import lombok.SneakyThrows;

public abstract class Unchecked {

    public interface Function<T, R> {
        @SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef"})
        static <T, R> java.util.function.Function<T, R> uncheckedFunction(Unchecked.Function<T, R> delegate) {
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

    public interface Supplier<T> {
        @SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef"})
        static <T> java.util.function.Supplier<T> uncheckedSupplier(Unchecked.Supplier<T> delegate) {
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

    public interface Consumer<T> {
        @SuppressWarnings({"Convert2Lambda", "Anonymous2MethodRef"})
        static <T> java.util.function.Consumer<T> uncheckedConsumer(Unchecked.Consumer<T> delegate) {
            return new java.util.function.Consumer<T>() {
                @Override
                @SneakyThrows
                public void accept(T t) {
                    delegate.accept(t);
                }
            };
        }

        void accept(T t) throws Exception;
    }
}
