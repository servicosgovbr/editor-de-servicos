package br.gov.servicos.editor.cartas;

import br.gov.servicos.editor.servicos.Metadados;
import com.google.common.cache.Cache;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCache;
import org.springframework.format.Formatter;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static br.gov.servicos.editor.config.CacheConfig.METADADOS;
import static java.util.Locale.getDefault;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SuppressFBWarnings("DMI_HARDCODED_ABSOLUTE_FILENAME")
@RunWith(MockitoJUnitRunner.class)
public class ListaDeConteudoTest {
    @Mock
    RepositorioGit repositorioGit;

    @Mock
    Formatter<Carta> formatter;

    @Mock
    Importador importador;

    @Mock
    Carta carta;

    @Mock
    CacheManager cacheManager;

    ListaDeConteudo listaDeConteudo;

    @Before
    public void setUp() throws Exception {
        listaDeConteudo = new ListaDeConteudo(importador, repositorioGit, formatter, cacheManager);

        Path dir = Files.createTempDirectory("listar-cartas-controller");
        Path v3 = dir.resolve("cartas-servico/v3/servicos");

        assertTrue(v3.toFile().mkdirs());
        Files.createFile(v3.resolve("id-qualquer.xml"));

        given(repositorioGit.getCaminhoAbsoluto())
                .willReturn(dir);

        given(formatter.parse("id-qualquer", getDefault()))
                .willReturn(carta);

    }

    @Test
    public void deveListarDiretorioDeCartas() throws Exception {
        Metadados<Carta.Servico> m1 = new Metadados<Carta.Servico>().withId("id-qualquer");
        given(carta.getMetadados()).willReturn(m1);

        Iterable<Metadados<Carta.Servico>> metadados = listaDeConteudo.listar();

        assertThat(metadados.iterator().next(), is(m1));
    }

    @Test(expected = FileNotFoundException.class)
    public void jogaExcecaoCasoDiretorioDeCartasNaoExista() throws Exception {
        given(repositorioGit.getCaminhoAbsoluto()).willReturn(Paths.get("/caminho/nao/existente"));

        listaDeConteudo.listar();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void forcaAtualizacaoDoCacheAoInicializar() throws Exception {
        Cache cache = mock(Cache.class);

        Metadados<Carta.Servico> m1 = new Metadados<Carta.Servico>().withId("id-qualquer");
        given(importador.isImportadoComSucesso()).willReturn(true);
        given(carta.getMetadados()).willReturn(m1);
        given(cacheManager.getCache(METADADOS)).willReturn(new GuavaCache(METADADOS, cache));


        listaDeConteudo.esquentarCacheDeMetadados();

        verify(cache).put("id-qualquer", m1);
    }
}
