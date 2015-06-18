package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.frontend.ExportadorServicoV2;
import br.gov.servicos.editor.xml.ArquivoXml;
import com.github.slugify.Slugify;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ImportadoresIntegrationTest {

    ImportadorServicoV1 v1;
    ExportadorServicoV2 exportadorV2;
    ImportadorServicoV2 v2;

    @Before
    public void setUp() throws Exception {
        File repositorioCartasLocal = new ClassPathResource("repositorio-cartas-servico").getFile();
        v1 = new ImportadorServicoV1(repositorioCartasLocal);
        exportadorV2 = new ExportadorServicoV2(new Slugify());
        v2 = new ImportadorServicoV2(repositorioCartasLocal);

    }

    @Test
    public void deveMigrarServicosDoFormatoV1ParaV2() throws Exception {
        Servico servicoV1 = v1.carregar("exemplo-servico-v1").get();
        Document doc = exportadorV2.exportar(servicoV1);
        Servico servicoV2 = v2.carregar(new ArquivoXml(doc)).get();

        assertThat(servicoV2, is(servicoV1));
    }
}