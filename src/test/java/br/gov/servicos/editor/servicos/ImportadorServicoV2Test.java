package br.gov.servicos.editor.servicos;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

import static br.gov.servicos.fixtures.TestData.SERVICO_V2;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

public class ImportadorServicoV2Test {

    File repositorioCartasLocal;

    @Before
    public void setUp() throws Exception {
        repositorioCartasLocal = new ClassPathResource("repositorio-cartas-servico").getFile();
    }

    @Test
    public void deveImportarServicoNoFormatoV2() throws Exception {
        Servico servico = new ImportadorServicoV2(repositorioCartasLocal).carregar("exemplo-servico-v2").get();

        assertThat(servico, samePropertyValuesAs(SERVICO_V2));
    }
}