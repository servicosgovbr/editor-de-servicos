package br.gov.servicos.editor.editar;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class IndiceCampoDeEtapaTest {

    @Test
    public void deveRetornarInstanciaDeIndiceCampoDeEtapa() throws Exception {
        assertThat(
                IndiceCampoDeEtapa.from("1,2"),
                is(new IndiceCampoDeEtapa()
                                .withIndiceEtapa(1)
                                .withIndiceCampo(2)));
    }

    @Test(expected = RuntimeException.class)
    public void deveFalharSeNaoContiverDoisIndices() throws Exception {
        IndiceCampoDeEtapa.from("1");
    }

    @Test(expected = NullPointerException.class)
    public void deveFalharNull() throws Exception {
        IndiceCampoDeEtapa.from(null);
    }

}