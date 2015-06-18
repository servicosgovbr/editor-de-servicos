package br.gov.servicos.editor.servicos;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

import static br.gov.servicos.fixtures.TestData.SERVICO_V1;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ImportadorServicoV1Test {

    File repositorioCartasLocal;

    @Before
    public void setUp() throws Exception {
        repositorioCartasLocal = new ClassPathResource("repositorio-cartas-servico").getFile();
    }

    @Test
    public void deveImportarServicoNoFormatoV1() throws Exception {
        Servico servico = new ImportadorServicoV1(repositorioCartasLocal).carregar("exemplo-servico-v1").get();

        assertThat(servico, is(SERVICO_V1));
    }
}