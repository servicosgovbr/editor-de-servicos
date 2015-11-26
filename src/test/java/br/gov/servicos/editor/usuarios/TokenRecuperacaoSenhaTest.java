package br.gov.servicos.editor.usuarios;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;


public class TokenRecuperacaoSenhaTest {
    private static final Integer TENTATIVAS_SOBRANDO = 3;

    @Test
    public void deveDecrementarTentativasSobrando() {
        TokenRecuperacaoSenha token = new TokenRecuperacaoSenha().withTentativasSobrando(TENTATIVAS_SOBRANDO);
        assertThat(token.decrementarTentativasSobrando().getTentativasSobrando(), equalTo(TENTATIVAS_SOBRANDO - 1));
    }

    @Test
    public void quandoChegarNoZeroNaoDeveDiminuirTentativasSobrando() {
        TokenRecuperacaoSenha token = new TokenRecuperacaoSenha().withTentativasSobrando(0);
        assertThat(token.decrementarTentativasSobrando().getTentativasSobrando(), equalTo(0));
    }

}