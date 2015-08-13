package br.gov.servicos.editor.cartas;

import br.gov.servicos.editor.servicos.Metadados;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.eclipse.jgit.transport.RefSpec;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

import static java.util.Locale.getDefault;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
@SuppressFBWarnings("DMI_HARDCODED_ABSOLUTE_FILENAME")
public class CartaTest {

    Carta carta;

    @Mock
    RepositorioGit repositorio;
    public static final Date HORARIO = new Date();
    public static final Metadados METADADOS = new Metadados()
            .withRevisao("da39a3ee5e6b4b0d3255bfef95601890afd80709")
            .withAutor("User Name")
            .withHorario(HORARIO);

    @Before
    public void setUp() throws Exception {
        carta = new Carta.Config().formatter(repositorio).parse("um-id-qualquer", getDefault());
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
        given(repositorio.getCaminhoAbsoluto())
                .willReturn(Paths.get("/um/caminho/qualquer"));

        assertThat(carta.getCaminhoAbsoluto(),
                is(Paths.get("/um/caminho/qualquer/cartas-servico/v3/servicos/um-id-qualquer.xml")));
    }

    @Test
    public void resolveCaminhoRelativo() throws Exception {
        given(repositorio.getCaminhoAbsoluto())
                .willReturn(Paths.get("/um/caminho/qualquer"));

        assertThat(carta.getCaminhoRelativo(),
                is(Paths.get("cartas-servico/v3/servicos/um-id-qualquer.xml")));
    }

    @Test
    public void buscaMetadadosDoUltimoCommitQuandoExisteBranch() throws Exception {
        given(repositorio.getCommitMaisRecenteDoBranch("refs/heads/um-id-qualquer"))
                .willReturn(Optional.of(METADADOS));

        assertThat(carta.getMetadados(), is(METADADOS.withId("um-id-qualquer")));
    }

    @Test
    public void buscaMetadadosDoUltimoCommitQuandoNaoExisteBranch() throws Exception {
        given(repositorio.getCommitMaisRecenteDoBranch("refs/heads/um-id-qualquer"))
                .willReturn(Optional.empty());

        given(repositorio.getCaminhoAbsoluto())
                .willReturn(Paths.get("/um/caminho/qualquer"));

        given(repositorio.getCommitMaisRecenteDoArquivo(Paths.get("cartas-servico/v3/servicos/um-id-qualquer.xml")))
                .willReturn(Optional.of(METADADOS));

        assertThat(carta.getMetadados(), is(METADADOS.withId("um-id-qualquer")));
    }

    @Test(expected = RuntimeException.class)
    public void jogaRuntimeExceptionQuandoNaoExisteNemNoBranchNemNoArquivo() throws Exception {
        given(repositorio.getCommitMaisRecenteDoBranch("refs/heads/um-id-qualquer"))
                .willReturn(Optional.empty());

        given(repositorio.getCaminhoAbsoluto())
                .willReturn(Paths.get("/um/caminho/qualquer"));

        given(repositorio.getCommitMaisRecenteDoArquivo(Paths.get("cartas-servico/v3/servicos/um-id-qualquer.xml")))
                .willReturn(Optional.empty());

        carta.getMetadados();
    }
}
