package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoMetadadosProvider;
import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.DeserializadorConteudoXML;
import br.gov.servicos.editor.frontend.Siorg;
import br.gov.servicos.editor.git.RepositorioGit;
import br.gov.servicos.editor.utils.EscritorDeArquivos;
import br.gov.servicos.editor.utils.LeitorDeArquivos;
import br.gov.servicos.editor.utils.ReformatadorXml;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;
import java.util.function.Supplier;

import static br.gov.servicos.editor.conteudo.TipoPagina.SERVICO;
import static br.gov.servicos.editor.utils.TestData.PROFILE;
import static java.util.Optional.of;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SuppressFBWarnings("DMI_HARDCODED_ABSOLUTE_FILENAME")
public class CartaTest {

    static final Date HORARIO = new Date();

    ConteudoVersionado conteudoVersionado;

    @Mock
    RepositorioGit repositorio;

    @Mock
    LeitorDeArquivos leitorDeArquivos;

    @Mock
    EscritorDeArquivos escritorDeArquivos;

    @Mock
    ReformatadorXml reformatadorXml;

    @Mock
    Siorg siorg;

    @Mock
    ConteudoMetadadosProvider provider;

    DeserializadorConteudoXML deserializadorConteudoXML;

    @Captor
    ArgumentCaptor<Supplier<Optional<String>>> captor;

    @Before
    public void setUp() throws Exception {
        deserializadorConteudoXML = new DeserializadorConteudoXML(ServicoXML.class);
        conteudoVersionado = new ConteudoVersionado("um-id-qualquer", SERVICO, repositorio, leitorDeArquivos, escritorDeArquivos, new Slugify(), reformatadorXml, siorg, deserializadorConteudoXML);
    }

    @Test
    public void resolveCaminhoAbsoluto() throws Exception {
        given(repositorio.getCaminhoAbsoluto())
                .willReturn(Paths.get("/um/caminho/qualquer"));

        assertThat(conteudoVersionado.getCaminhoAbsoluto(),
                is(Paths.get("/um/caminho/qualquer/", SERVICO.getCaminhoPasta().toString(), "um-id-qualquer.xml")));
    }

    @Test
    public void resolveCaminhoRelativo() throws Exception {
        given(repositorio.getCaminhoAbsoluto())
                .willReturn(Paths.get("/um/caminho/qualquer"));

        assertThat(conteudoVersionado.getCaminhoRelativo(),
                is(Paths.get(SERVICO.getCaminhoPasta().toString(), "um-id-qualquer.xml")));
    }

    @Test
    public void retornaConteudoDoArquivoNoBranch() throws Exception {
        given(
                repositorio.comRepositorioAbertoNoBranch(
                        eq("refs/heads/servico-um-id-qualquer"),
                        captor.capture()))
                .willReturn(of("<servico/>"));

        given(repositorio.getCaminhoAbsoluto())
                .willReturn(Paths.get("/um/caminho/qualquer"));

        given(leitorDeArquivos.ler(Paths.get("/um/caminho/qualquer/", SERVICO.getCaminhoPasta().toString(), "um-id-qualquer.xml").toFile()))
                .willReturn(of("<servico/>"));

        assertThat(conteudoVersionado.getConteudoRaw(), is("<servico/>"));
        assertThat(captor.getValue().get().get(), is("<servico/>"));
    }

    @Test
    public void retornaConteudoVazioQuandoArquivoNaoExisteNoBranch() throws Exception {
        given(repositorio.comRepositorioAbertoNoBranch(eq("refs/heads/servico-um-id-qualquer"), captor.capture()))
                .willReturn(of("<servico/>"));

        assertThat(conteudoVersionado.getConteudoRaw(), is("<servico/>"));

        given(repositorio.getCaminhoAbsoluto())
                .willReturn(Paths.get("/um/caminho/qualquer"));

        given(leitorDeArquivos.ler(new File("/um/caminho/qualquer/" + SERVICO.getCaminhoPasta() + "/um-id-qualquer.xml")))
                .willReturn(Optional.empty());

        assertThat(captor.getValue().get().isPresent(), is(false));
    }

    @Test(expected = FileNotFoundException.class)
    public void jogaFileNotFoundExceptionCasoNaoHajaArquivoNoBranch() throws Exception {
        given(repositorio.getCaminhoAbsoluto())
                .willReturn(Paths.get("/um/caminho/qualquer"));

        given(repositorio.comRepositorioAbertoNoBranch(
                eq("refs/heads/servico-um-id-qualquer"),
                any()))
                .willReturn(Optional.empty());

        conteudoVersionado.getConteudoRaw();
    }

    @Test
    public void salvaConteudoNoBranch() throws Exception {
        given(repositorio.comRepositorioAbertoNoBranch(eq("refs/heads/servico-um-id-qualquer"), captor.capture()))
                .willReturn(null);

        conteudoVersionado.salvar(PROFILE, "<servico/>");

        given(repositorio.getCaminhoAbsoluto())
                .willReturn(Paths.get("/um/caminho/qualquer"));

        captor.getValue().get();

        verify(repositorio).pull();

        Path caminho = Paths.get(SERVICO.getCaminhoPasta() + "/um-id-qualquer.xml");

        verify(repositorio).add(caminho);
        verify(repositorio).commit(caminho, "Cria 'um-id-qualquer'", PROFILE);
        verify(repositorio).push("refs/heads/servico-um-id-qualquer");
    }
}
