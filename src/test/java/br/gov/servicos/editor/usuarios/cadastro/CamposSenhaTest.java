package br.gov.servicos.editor.usuarios.cadastro;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CamposSenhaTest {
    public static final String SENHA = "1234";
    public static final String OUTRA_SENHA = "9999";

    @Test
    public void campoValidoSeSenhasSaoIguais() {
        CamposSenha camposSenha = new CamposSenha().withSenha(SENHA).withConfirmacaoSenha(SENHA);
        assertTrue(camposSenha.isValid());
    }

    @Test
    public void campoInvalidoSeSenhasSaoDiferentes() {
        CamposSenha camposSenha = new CamposSenha().withSenha(SENHA).withConfirmacaoSenha(OUTRA_SENHA);
        assertFalse(camposSenha.isValid());
    }

}