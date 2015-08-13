package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.cartas.Carta;
import br.gov.servicos.editor.cartas.RepositorioGit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.format.Formatter;

import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Locale.getDefault;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ListarCartasControllerTest {

    ListarCartasController controller;

    @Mock
    RepositorioGit repositorioGit;

    @Mock
    Formatter<Carta> formatter;

    @Mock
    Carta carta;

    @Before
    public void setUp() throws Exception {
        controller = new ListarCartasController(repositorioGit, formatter);
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

        Iterable<Metadados> metadados = controller.listar();

        assertThat(metadados.iterator().next(), is(m1));
    }
}