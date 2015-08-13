package br.gov.servicos.editor.cartas;

import br.gov.servicos.editor.utils.EscritorDeArquivos;
import br.gov.servicos.editor.utils.LeitorDeArquivos;
import com.github.slugify.Slugify;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Locale.getDefault;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class CartaFactoryTest {

    Carta.Factory factory;

    @Mock
    LeitorDeArquivos leitorDeArquivos;

    @Mock
    EscritorDeArquivos escritorDeArquivos;

    @Mock
    RepositorioGit repositorio;

    @Before
    public void setUp() throws Exception {
        factory = new Carta.Factory(new Slugify(), repositorio, leitorDeArquivos, escritorDeArquivos);
    }

    @Test
    public void geraSlugComIdRecebido() throws Exception {
        assertThat(factory.parse("Um Título", getDefault()).getId(), is("um-titulo"));
    }

    @Test
    public void geraStringComId() throws Exception {
        Carta carta = mock(Carta.class);
        given(carta.getId()).willReturn("um-id");

        assertThat(factory.print(carta, getDefault()), is("um-id"));

    }
}