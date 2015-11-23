package br.gov.servicos.editor.usuarios;


public class UsuarioInexistenteException extends RuntimeException {
    public UsuarioInexistenteException() {
        super("Usuário não existe");
    }
}
