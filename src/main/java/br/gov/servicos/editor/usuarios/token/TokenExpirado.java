package br.gov.servicos.editor.usuarios.token;

import lombok.Getter;

public class TokenExpirado extends TokenInvalido {
    @Getter
    public static class CpfTokenInvalido extends TokenInvalido {
        private int tentativasSobrando;

        public CpfTokenInvalido(int tentativasSobrando) {
            this.tentativasSobrando = tentativasSobrando;
        }
    }
}
