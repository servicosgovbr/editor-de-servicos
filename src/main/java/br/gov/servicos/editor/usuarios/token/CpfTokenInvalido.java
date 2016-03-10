package br.gov.servicos.editor.usuarios.token;

import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CpfTokenInvalido extends TokenInvalido {

    @Getter
    int tentativasSobrando;

    public CpfTokenInvalido(int tentativasSobrando) {
        this.tentativasSobrando = tentativasSobrando;
    }
}