package br.gov.servicos.editor.editar;

import org.junit.Test;

import static br.gov.servicos.editor.editar.IndiceCampoDeEtapa.from;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class IndiceCampoDeEtapaTest {

    @Test
    public void deveRetornarInstanciaDeIndiceCampoDeEtapa() throws Exception {
        assertThat(from("1,2").getEtapa(), is(1));
        assertThat(from("1,2").getCampo(), is(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveFalharSeNaoContiverDoisIndices() throws Exception {
        from("1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveFalharSeStringNaoForCompreensivel() throws Exception {
        from("foo,bar");
    }

    @Test(expected = IllegalArgumentException.class)
    public void deveFalharNull() throws Exception {
        from(null);
    }

}