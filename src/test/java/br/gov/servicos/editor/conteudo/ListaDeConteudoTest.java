package br.gov.servicos.editor.conteudo;

import br.gov.servicos.editor.conteudo.cartas.Carta;
import br.gov.servicos.editor.conteudo.paginas.Pagina;
import br.gov.servicos.editor.conteudo.paginas.PaginaVersionada;
import br.gov.servicos.editor.conteudo.paginas.PaginaVersionadaFactory;
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
import java.util.Collection;
import java.util.stream.Stream;

import static br.gov.servicos.editor.config.CacheConfig.METADADOS;
import static java.util.Locale.getDefault;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SuppressFBWarnings("DMI_HARDCODED_ABSOLUTE_FILENAME")
@RunWith(MockitoJUnitRunner.class)
public class ListaDeConteudoTest {

    @Mock
    RepositorioGit repositorioGit;

    @Mock
    Formatter<Carta> formatterCarta;

    @Mock
    PaginaVersionadaFactory factory;

    @Mock
    Importador importador;

    @Mock
    Carta carta;

    @Mock
    PaginaVersionada paginaVersionada;

    @Mock
    CacheManager cacheManager;

    ListaDeConteudo listaDeConteudo;

    @Before
    public void setUp() throws Exception {


        listaDeConteudo = new ListaDeConteudo(importador, repositorioGit, formatterCarta, factory, cacheManager, true);

        Path dir = Files.createTempDirectory("listar-cartas-controller");
        Path servicos = dir.resolve("cartas-servico/v3/servicos");
        Path orgaos = dir.resolve("conteudo/orgaos");

        assertTrue(servicos.toFile().mkdirs());
        assertTrue(orgaos.toFile().mkdirs());

        Files.createFile(servicos.resolve("id-qualquer.xml"));
        Files.createFile(orgaos.resolve("outro-id-qualquer.md"));

        given(repositorioGit.getCaminhoAbsoluto()).willReturn(dir);
        given(formatterCarta.parse("id-qualquer", getDefault())).willReturn(carta);
        given(factory.pagina("outro-id-qualquer", any()))
                .willReturn(paginaVersionada);
    }

    @Test
    public void deveListarDiretorioDeCartas() throws Exception {
        Metadados<Carta.Servico> m1 = new Metadados<Carta.Servico>().withId("id-qualquer");

        given(repositorioGit.branches()).will(i -> Stream.empty());
        given(paginaVersionada.getMetadados()).willReturn(new Metadados<>());
        given(carta.getMetadados()).willReturn(m1);

        Collection<Metadados<?>> metadados = listaDeConteudo.listar();

        assertThat(metadados, hasItem(m1));
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
        Metadados<Pagina> m2 = new Metadados<Pagina>().withId("outro-id-qualquer");

        given(importador.isImportadoComSucesso()).willReturn(true);
        given(repositorioGit.branches()).will(i -> Stream.empty());
        given(carta.getMetadados()).willReturn(m1);
        given(paginaVersionada.getMetadados()).willReturn(m2);

        given(cacheManager.getCache(METADADOS)).willReturn(new GuavaCache(METADADOS, cache));

        listaDeConteudo.esquentarCacheDeMetadados();

        verify(cache).put("id-qualquer", m1);
        verify(cache).put("outro-id-qualquer", m2);
    }
}
