package br.gov.servicos.editor.conteudo;

import br.gov.servicos.editor.conteudo.cartas.Carta;
import br.gov.servicos.editor.conteudo.paginas.Pagina;
import br.gov.servicos.editor.conteudo.paginas.PaginaVersionada;
import br.gov.servicos.editor.conteudo.paginas.PaginaVersionadaFactory;
import br.gov.servicos.editor.conteudo.paginas.TipoPagina;
import com.google.common.cache.Cache;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static br.gov.servicos.editor.config.CacheConfig.METADADOS;
import static br.gov.servicos.editor.conteudo.paginas.TipoPagina.*;
import static br.gov.servicos.editor.utils.Unchecked.Function.uncheckedFunction;
import static java.util.Locale.getDefault;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.concat;
import static lombok.AccessLevel.PRIVATE;
import static org.eclipse.jgit.lib.Constants.MASTER;

@Slf4j
@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ListaDeConteudo {

    Importador importador;
    RepositorioGit repositorioGit;
    Formatter<Carta> formatterCarta;
    PaginaVersionadaFactory paginaVersionadaFactory;
    CacheManager cacheManager;
    boolean esquentarCache;

    @Autowired
    public ListaDeConteudo(
            Importador importador,
            RepositorioGit repositorioGit,
            Formatter<Carta> formatterCarta,
            PaginaVersionadaFactory paginaVersionadaFactory,
            CacheManager cacheManager,
            @Value("${flags.esquentar.cache}") boolean esquentarCache
    ) {
        this.importador = importador;
        this.repositorioGit = repositorioGit;
        this.formatterCarta = formatterCarta;
        this.paginaVersionadaFactory = paginaVersionadaFactory;
        this.cacheManager = cacheManager;
        this.esquentarCache = esquentarCache;
    }

    @PostConstruct
    @SneakyThrows
    public void esquentarCacheDeMetadados() {
        if (!esquentarCache) {
            return;
        }

        if (importador.isImportadoComSucesso()) {
            @SuppressWarnings("unchecked")
            Cache<String, Metadados<?>> metadados =
                    (Cache) cacheManager.getCache(METADADOS).getNativeCache();

            listar().forEach(c -> metadados.put(c.getId(), c));
            log.info("Cache de metadados das cartas criado com sucesso");

        } else {
            log.warn("Cache de metadados das cartas não foi criado - houve algum problema com o clone do repositório?");
        }
    }

    @SneakyThrows
    public boolean isIdUnico(String id) {
        return listar().stream().noneMatch(m -> m.getId().equals(id));
    }

    public Set<Metadados<?>> listar() throws FileNotFoundException {
        Stream<Metadados<Carta.Servico>> servicos = concat(listar("cartas-servico/v3/servicos", "xml"), repositorioGit.branches().filter(n -> !n.equals(MASTER)))
                .map(uncheckedFunction(id -> formatterCarta.parse(id, getDefault())))
                .map(Carta::getMetadados);

        Stream<Metadados<Pagina>> orgaos = listarMetadados(ORGAO);
        Stream<Metadados<Pagina>> areasInteresse = listarMetadados(AREA_INTERESSE);
        Stream<Metadados<Pagina>> paginasEspeciais = listarMetadados(ESPECIAL);

        return concat(concat(concat(servicos, orgaos), areasInteresse), paginasEspeciais).collect(toSet());
    }

    private Stream<Metadados<Pagina>> listarMetadados(TipoPagina tipo) throws FileNotFoundException {
        return concat(listar("conteudo/" + tipo.getNomePasta(), "md"), repositorioGit.branches().filter(n -> !n.equals(MASTER)))
                .map(uncheckedFunction(id -> paginaVersionadaFactory.pagina(id, tipo)))
                .map(PaginaVersionada::getMetadados);
    }

    private Stream<String> listar(String caminho, String ext) throws FileNotFoundException {
        File dir = repositorioGit.getCaminhoAbsoluto().resolve(caminho).toFile();
        if (!dir.exists()) {
            throw new FileNotFoundException("Diretório " + dir + " não encontrado!");
        }

        File[] arquivos = Optional
                .ofNullable(dir.listFiles((x, name) -> name.endsWith('.' + ext)))
                .orElse(new File[0]);

        log.info("{} arquivos encontrados em {}", arquivos.length, caminho);

        return Stream.of(arquivos)
                .map(f -> f.getName().replaceAll("\\." + ext + '$', ""));
    }

}
