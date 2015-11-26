package br.gov.servicos.editor.usuarios;


import lombok.Getter;

@Getter
public class TokenInvalido extends Throwable {
    private int tentativasSobrando;

    public TokenInvalido(int tentativasSobrando) {
        this.tentativasSobrando = tentativasSobrando;
    }
}

