package br.gov.servicos.editor.usuarios.token;

import lombok.Getter;

public class CpfTokenInvalido extends TokenInvalido {

    @Getter
    private int tentativasSobrando;

    public CpfTokenInvalido(int tentativasSobrando) {
        this.tentativasSobrando = tentativasSobrando;
    }
}