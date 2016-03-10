package br.gov.servicos.editor.usuarios.token;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


public class TokenTest {
    private static final Integer TENTATIVAS_SOBRANDO = 3;

    @Test
    public void deveDecrementarTentativasSobrando() {
        Token token = new Token().withTentativasSobrando(TENTATIVAS_SOBRANDO);
        assertThat(token.decrementarTentativasSobrando().getTentativasSobrando(), equalTo(TENTATIVAS_SOBRANDO - 1));
    }

    @Test
    public void quandoChegarNoZeroNaoDeveDiminuirTentativasSobrando() {
        Token token = new Token().withTentativasSobrando(0);
        assertThat(token.decrementarTentativasSobrando().getTentativasSobrando(), equalTo(0));
    }

}