package br.gov.servicos.editor.cartas;

import br.gov.servicos.editor.servicos.Metadados;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.format.Formatter;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Locale.getDefault;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

@SuppressFBWarnings("DMI_HARDCODED_ABSOLUTE_FILENAME")
@RunWith(MockitoJUnitRunner.class)
public class ListaDeCartasTest {
    @Mock
    RepositorioGit repositorioGit;

    @Mock
    Formatter<Carta> formatter;

    @Mock
    Carta carta;

    ListaDeCartas listaDeCartas;

    @Before
    public void setUp() throws Exception {
        listaDeCartas = new ListaDeCartas(repositorioGit, formatter);
    }

    @Test
    public void deveListarDiretorioDeCartas() throws Exception {
        Metadados m1 = new Metadados().withId("id-qualquer");

        Path dir = Files.createTempDirectory("listar-cartas-controller");
        Path v3 = dir.resolve("cartas-servico/v3/servicos");

        assertTrue(v3.toFile().mkdirs());
        Files.createFile(v3.resolve("id-qualquer.xml"));

        given(repositorioGit.getCaminhoAbsoluto())
                .willReturn(dir);

        given(formatter.parse("id-qualquer", getDefault()))
                .willReturn(carta);

        given(carta.getMetadados()).willReturn(m1);

        Iterable<Metadados> metadados = listaDeCartas.listar();

        assertThat(metadados.iterator().next(), is(m1));
    }

    @Test(expected = FileNotFoundException.class)
    public void jogaExcecaoCasoDiretorioDeCartasNaoExista() throws Exception {
        given(repositorioGit.getCaminhoAbsoluto()).willReturn(Paths.get("/caminho/nao/existente"));

        listaDeCartas.listar();
    }

}
