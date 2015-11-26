package br.gov.servicos.editor.usuarios;


import lombok.Getter;

@Getter
public class CpfTokenInvalido extends TokenInvalido {
    private int tentativasSobrando;

    public CpfTokenInvalido(int tentativasSobrando) {
        this.tentativasSobrando = tentativasSobrando;
    }
}

