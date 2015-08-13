package br.gov.servicos.editor.frontend;

import org.junit.Test;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class ErrosHttpTest {

    @Test
    public void testFileNotFound() throws Exception {
        Method metodo = ErrosHttp.class.getDeclaredMethod("fileNotFound");

        assertThat(metodo.getAnnotation(ResponseStatus.class).value(),
                is(NOT_FOUND));

        assertThat(metodo.getAnnotation(ExceptionHandler.class).value(),
                is(new Class[]{FileNotFoundException.class}));
    }
}