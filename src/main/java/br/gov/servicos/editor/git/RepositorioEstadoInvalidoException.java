package br.gov.servicos.editor.git;


public class RepositorioEstadoInvalidoException extends IllegalStateException {

    public RepositorioEstadoInvalidoException() {
        super();
    }

    public RepositorioEstadoInvalidoException(String s) {
        super(s);
    }

    public RepositorioEstadoInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositorioEstadoInvalidoException(Throwable cause) {
        super(cause);
    }

}
