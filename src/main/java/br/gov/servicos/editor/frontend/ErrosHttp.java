package br.gov.servicos.editor.frontend;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.FileNotFoundException;

@ControllerAdvice
class ErrosHttp {

    @ResponseStatus(HttpStatus.NOT_FOUND)  // 409
    @ExceptionHandler(FileNotFoundException.class)
    public void fileNotFound() {
    }
}
