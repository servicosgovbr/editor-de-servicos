package br.gov.servicos.editor.cartas;

import org.eclipse.jgit.transport.RefSpec;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;

import static java.util.Locale.getDefault;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CartaTest {

    Carta carta;

    @Before
    public void setUp() throws Exception {
        carta = new Carta.Factory().cartaFormatter(new File("/um/caminho/qualquer")).parse("um-id-qualquer", getDefault());
    }

    @Test
    public void testGetRefSpec() throws Exception {
        assertThat(carta.getRefSpec(), is(new RefSpec("um-id-qualquer:um-id-qualquer")));
    }

    @Test
    public void testGetBranchRef() throws Exception {
        assertThat(carta.getBranchRef(), is("refs/heads/um-id-qualquer"));
    }

    @Test
    public void testCaminhoAbsoluto() throws Exception {
        assertThat(carta.caminhoAbsoluto(), is(Paths.get("/um/caminho/qualquer/cartas-servico/v3/servicos/um-id-qualquer.xml")));
    }

    @Test
    public void testCaminhoRelativo() throws Exception {
        assertThat(carta.caminhoRelativo(), is(Paths.get("cartas-servico/v3/servicos/um-id-qualquer.xml")));
    }
}