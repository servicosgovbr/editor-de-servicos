package br.gov.servicos.editor.frontend;

import org.junit.Test;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.ResourceAccessException;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

public class ErrosHttpTest {

    @Test
    public void fileNotFoundEstáDeclaradoCorretamente() throws Exception {
        Method metodo = ErrosHttp.class.getDeclaredMethod("fileNotFound");

        assertThat(metodo.getAnnotation(ResponseStatus.class).value(),
                is(NOT_FOUND));

        assertThat(metodo.getAnnotation(ExceptionHandler.class).value(),
                is(new Class[]{FileNotFoundException.class}));
    }

    @Test
    public void fileNotFoundNãoFazNada() throws Exception {
        new ErrosHttp().fileNotFound();
    }

    @Test
    public void erroAoDelegarParaApiExternaEstáDeclaradoCorretamente() throws Exception {
        Method metodo = ErrosHttp.class.getDeclaredMethod("erroAoDelegarParaApiExterna");

        assertThat(metodo.getAnnotation(ResponseStatus.class).value(),
                is(OK));

        assertThat(metodo.getAnnotation(ExceptionHandler.class).value(),
                is(new Class[]{ResourceAccessException.class}));
    }

    @Test
    public void erroAoDelegarParaApiExternaRetornaArrayVazioEmJson() throws Exception {
        assertThat(new ErrosHttp().erroAoDelegarParaApiExterna(), is("[]"));
    }
}