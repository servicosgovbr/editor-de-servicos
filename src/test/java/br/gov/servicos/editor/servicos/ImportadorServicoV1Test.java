package br.gov.servicos.editor.servicos;

import com.github.slugify.Slugify;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

import static br.gov.servicos.fixtures.TestData.SERVICO_V1;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

public class ImportadorServicoV1Test {

    File repositorioCartasLocal;
    Slugify slugify;

    @Before
    public void setUp() throws Exception {
        repositorioCartasLocal = new ClassPathResource("repositorio-cartas-servico").getFile();
        slugify = new Slugify();
    }

    @Test
    public void deveImportarServicoNoFormatoV1() throws Exception {
        Servico servico = new ImportadorServicoV1(repositorioCartasLocal, slugify).carregar("exemplo-servico-v1").get();

        assertThat(servico, samePropertyValuesAs(SERVICO_V1));
    }
}