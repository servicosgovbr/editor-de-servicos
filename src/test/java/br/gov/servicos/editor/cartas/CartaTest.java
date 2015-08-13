package br.gov.servicos.editor.cartas;

import br.gov.servicos.editor.servicos.Metadados;
import br.gov.servicos.editor.utils.LeitorDeArquivos;
import com.github.slugify.Slugify;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Locale.getDefault;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;

@RunWith(MockitoJUnitRunner.class)
@SuppressFBWarnings("DMI_HARDCODED_ABSOLUTE_FILENAME")
public class CartaTest {

    public static final Date HORARIO = new Date();

    public static final Metadados METADADOS = new Metadados()
            .withRevisao("da39a3ee5e6b4b0d3255bfef95601890afd80709")
            .withAutor("User Name")
            .withHorario(HORARIO);

    Carta carta;

    @Mock
    RepositorioGit repositorio;

    @Mock
    LeitorDeArquivos leitorDeArquivos;

    @Captor
    ArgumentCaptor<Supplier<Optional<String>>> captor;

    @Before
    public void setUp() throws Exception {
        carta = new Carta.Formatter(new Slugify(), repositorio, leitorDeArquivos)
                .parse("um-id-qualquer", getDefault());
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

    @Test
    public void retornaConteudoDoArquivoNoBranch() throws Exception {
        given(repositorio.comRepositorioAbertoNoBranch(eq("refs/heads/um-id-qualquer"), captor.capture()))
                .willReturn(Optional.of("<servico/>"));

        assertThat(carta.getConteudo(), is("<servico/>"));

        given(repositorio.getCaminhoAbsoluto())
                .willReturn(Paths.get("/um/caminho/qualquer"));

        given(leitorDeArquivos.ler(new File("/um/caminho/qualquer/cartas-servico/v3/servicos/um-id-qualquer.xml")))
                .willReturn(Optional.of("<servico/>"));

        assertThat(captor.getValue().get().get(), is("<servico/>"));
    }

    @Test
    public void retornaConteudoVazioQuandoArquivoNaoExisteNoBranch() throws Exception {
        given(repositorio.comRepositorioAbertoNoBranch(eq("refs/heads/um-id-qualquer"), captor.capture()))
                .willReturn(Optional.of("<servico/>"));

        assertThat(carta.getConteudo(), is("<servico/>"));

        given(repositorio.getCaminhoAbsoluto())
                .willReturn(Paths.get("/um/caminho/qualquer"));

        given(leitorDeArquivos.ler(new File("/um/caminho/qualquer/cartas-servico/v3/servicos/um-id-qualquer.xml")))
                .willReturn(Optional.empty());

        assertThat(captor.getValue().get().isPresent(), is(false));
    }

    @Test(expected = FileNotFoundException.class)
    public void jogaFileNotFoundExceptionCasoNaoHajaArquivoNoBranch() throws Exception {
        given(repositorio.comRepositorioAbertoNoBranch(eq("refs/heads/um-id-qualquer"), anyObject())).willReturn(Optional.empty());

        carta.getConteudo();
    }
}
