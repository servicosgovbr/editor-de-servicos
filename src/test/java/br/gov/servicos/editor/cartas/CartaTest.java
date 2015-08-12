package br.gov.servicos.editor.cartas;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.RefSpec;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.nio.file.Paths;

import static java.util.Locale.getDefault;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
@SuppressFBWarnings("DMI_HARDCODED_ABSOLUTE_FILENAME")
public class CartaTest {

    @Mock
    Git git;

    Carta carta;

    @Before
    public void setUp() throws Exception {
        carta = new Carta.Config().formatter(new File("/um/caminho/qualquer")).parse("um-id-qualquer", getDefault());
    }

    @Test
    public void formataRefSpecComBranchLocalERemoto() throws Exception {
        assertThat(carta.getRefSpec(),
                is(new RefSpec("um-id-qualquer:um-id-qualquer")));
    }

    @Test
    public void formataBranchLocal() throws Exception {
        assertThat(carta.getBranchRef(),
                is("refs/heads/um-id-qualquer"));
    }

    @Test
    public void resolveCaminhoAbsoluto() throws Exception {
        assertThat(carta.getCaminhoAbsoluto(),
                is(Paths.get("/um/caminho/qualquer/cartas-servico/v3/servicos/um-id-qualquer.xml")));
    }

    @Test
    public void resolveCaminhoRelativo() throws Exception {
        assertThat(carta.getCaminhoRelativo(),
                is(Paths.get("cartas-servico/v3/servicos/um-id-qualquer.xml")));
    }

    @Test @Ignore
    public void testMetadados() {
        carta.metadados(git);
    }


}