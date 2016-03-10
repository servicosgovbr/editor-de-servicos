package br.gov.servicos.editor.git;


public class RepositorioEstadoInvalidoException extends IllegalStateException {

    public RepositorioEstadoInvalidoException(String message) {
        super(message);
    }

    public RepositorioEstadoInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
}
