package br.gov.servicos.editor.cartas;

import br.gov.servicos.editor.servicos.Metadados;
import br.gov.servicos.editor.servicos.Revisao;
import br.gov.servicos.editor.utils.EscritorDeArquivos;
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
import org.springframework.security.core.userdetails.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SuppressFBWarnings("DMI_HARDCODED_ABSOLUTE_FILENAME")
public class CartaTest {

    static final Date HORARIO = new Date();

    static final Revisao REVISAO = new Revisao().withHash("da39a3ee5e6b4b0d3255bfef95601890afd80709")
            .withAutor("User Name")
            .withHorario(HORARIO);

    static final Metadados<Carta.Servico> METADADOS = new Metadados<Carta.Servico>().withId("um-id-qualquer");

    Carta carta;

    @Mock
    RepositorioGit repositorio;

    @Mock
    LeitorDeArquivos leitorDeArquivos;

    @Mock
    EscritorDeArquivos escritorDeArquivos;

    @Captor
    ArgumentCaptor<Supplier<Optional<String>>> captor;

    @Before
    public void setUp() throws Exception {
        carta = new Carta.Factory(new Slugify(), repositorio, leitorDeArquivos, escritorDeArquivos)
                .carta("um-id-qualquer");
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
        given(repositorio.getRevisaoMaisRecenteDoArquivo(Paths.get("cartas-servico/v3/servicos/um-id-qualquer.xml")))
                .willReturn(Optional.empty());

        given(repositorio.getCaminhoAbsoluto())
                .willReturn(Paths.get("/um/caminho/qualquer"));

        given(repositorio.getRevisaoMaisRecenteDoBranch("refs/heads/um-id-qualquer"))
                .willReturn(of(REVISAO));

        assertThat(carta.getMetadados(), is(METADADOS.withPublicado(null).withEditado(REVISAO)));
    }

    @Test
    public void buscaMetadadosDoUltimoCommitQuandoNaoExisteBranch() throws Exception {
        given(repositorio.getRevisaoMaisRecenteDoBranch("refs/heads/um-id-qualquer"))
                .willReturn(empty());

        given(repositorio.getCaminhoAbsoluto())
                .willReturn(Paths.get("/um/caminho/qualquer"));

        given(repositorio.getRevisaoMaisRecenteDoArquivo(Paths.get("cartas-servico/v3/servicos/um-id-qualquer.xml")))
                .willReturn(of(REVISAO));

        assertThat(carta.getMetadados(), is(METADADOS.withPublicado(REVISAO).withEditado(null)));
    }

    @Test
    public void retornaConteudoDoArquivoNoBranch() throws Exception {
        given(repositorio.comRepositorioAbertoNoBranch(eq("refs/heads/um-id-qualquer"), captor.capture()))
                .willReturn(of("<servico/>"));

        assertThat(carta.getConteudoRaw(), is("<servico/>"));

        given(repositorio.getCaminhoAbsoluto())
                .willReturn(Paths.get("/um/caminho/qualquer"));

        given(leitorDeArquivos.ler(new File("/um/caminho/qualquer/cartas-servico/v3/servicos/um-id-qualquer.xml")))
                .willReturn(of("<servico/>"));

        assertThat(captor.getValue().get().get(), is("<servico/>"));
    }

    @Test
    public void retornaConteudoVazioQuandoArquivoNaoExisteNoBranch() throws Exception {
        given(repositorio.comRepositorioAbertoNoBranch(eq("refs/heads/um-id-qualquer"), captor.capture()))
                .willReturn(of("<servico/>"));

        assertThat(carta.getConteudoRaw(), is("<servico/>"));

        given(repositorio.getCaminhoAbsoluto())
                .willReturn(Paths.get("/um/caminho/qualquer"));

        given(leitorDeArquivos.ler(new File("/um/caminho/qualquer/cartas-servico/v3/servicos/um-id-qualquer.xml")))
                .willReturn(Optional.empty());

        assertThat(captor.getValue().get().isPresent(), is(false));
    }

    @Test(expected = FileNotFoundException.class)
    public void jogaFileNotFoundExceptionCasoNaoHajaArquivoNoBranch() throws Exception {
        given(repositorio.comRepositorioAbertoNoBranch(eq("refs/heads/um-id-qualquer"), anyObject())).willReturn(Optional.empty());

        carta.getConteudoRaw();
    }

    @Test
    public void salvaConteudoNoBranch() throws Exception {
        User usuario = new User("Fulano de Tal", "", emptyList());

        given(repositorio.comRepositorioAbertoNoBranch(eq("refs/heads/um-id-qualquer"), captor.capture()))
                .willReturn(null);

        carta.salvar(usuario, "<servico/>");

        given(repositorio.getCaminhoAbsoluto())
                .willReturn(Paths.get("/um/caminho/qualquer"));

        captor.getValue().get();

        verify(repositorio).pull();

        Path caminho = Paths.get("cartas-servico/v3/servicos/um-id-qualquer.xml");

        verify(repositorio).add(caminho);
        verify(repositorio).commit(caminho, "Cria 'um-id-qualquer'", usuario);
        verify(repositorio).push("refs/heads/um-id-qualquer");
    }
}
