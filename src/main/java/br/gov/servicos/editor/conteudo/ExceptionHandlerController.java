package br.gov.servicos.editor.conteudo;

import br.gov.servicos.editor.conteudo.cartas.ConteudoInexistenteException;
import br.gov.servicos.editor.conteudo.cartas.ImportacaoXmlException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(ConteudoInexistenteException.class)
    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    public String conteudoInexistente(ConteudoInexistenteException e) {
        return e.getMessage();
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(NOT_ACCEPTABLE)
    @ResponseBody
    public String estadoInvalido(IllegalStateException e) {
        return e.getMessage();
    }

    @ExceptionHandler(ImportacaoXmlException.class)
    @ResponseStatus(NOT_ACCEPTABLE)
    @ResponseBody
    public String erroImportacaoXml(ImportacaoXmlException e) {
        return e.getMessage();
    }
}
