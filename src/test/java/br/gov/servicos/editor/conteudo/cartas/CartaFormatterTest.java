package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.git.RepositorioGit;
import br.gov.servicos.editor.utils.EscritorDeArquivos;
import br.gov.servicos.editor.utils.LeitorDeArquivos;
import br.gov.servicos.editor.utils.ReformatadorXml;
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
public class CartaFormatterTest {

    Carta.Formatter formatter;

    @Mock
    LeitorDeArquivos leitorDeArquivos;

    @Mock
    EscritorDeArquivos escritorDeArquivos;

    @Mock
    RepositorioGit repositorio;

    @Mock
    ReformatadorXml reformatadorXml;

    @Before
    public void setUp() throws Exception {
        formatter = new Carta.Formatter(new Carta.Factory(new Slugify(), repositorio, leitorDeArquivos, escritorDeArquivos, reformatadorXml));
    }

    @Test
    public void geraSlugComIdRecebido() throws Exception {
        assertThat(formatter.parse("Um Título", getDefault()).getId(), is("um-titulo"));
    }

    @Test
    public void geraStringComId() throws Exception {
        Carta carta = mock(Carta.class);
        given(carta.getId()).willReturn("um-id");

        assertThat(formatter.print(carta, getDefault()), is("um-id"));

    }
}