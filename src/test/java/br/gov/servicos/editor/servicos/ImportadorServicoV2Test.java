package br.gov.servicos.editor.servicos;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

import static br.gov.servicos.fixtures.TestData.SERVICO_V2;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ImportadorServicoV2Test {

    File repositorioCartasLocal;

    @Before
    public void setUp() throws Exception {
        repositorioCartasLocal = new ClassPathResource("repositorio-cartas-servico").getFile();
    }

    @Test
    public void deveImportarServicoNoFormatoV1() throws Exception {
        Servico servico = new ImportadorServicoV2(repositorioCartasLocal).carregar("exemplo-servico-v2");

        assertThat(servico, is(SERVICO_V2));
    }
}