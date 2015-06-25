package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.config.Vcge20Config;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MapaVcge20Test extends TestCase {

    @Test
    public void testAreaDeInteresse() throws Exception {
        MapaVcge20 mapa = new Vcge20Config().getMapaVcge20();
        assertThat(mapa.areaDeInteresse("analise-de-risco-area-vegetal", "Análise de risco - área vegetal"), hasItem(new AreaDeInteresse()
                .withId("agropecuaria")
                .withArea("Agropecuária")));
    }

}