package br.gov.servicos.editor.config;

import br.gov.servicos.editor.cartas.Carta;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Arrays.asList;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String METADADOS = "metadados";
    public static final String ORGAOS = "orgaos";
    public static final String ORGAO_SIORG = "orgaoSiorg";
    public static final String VCGE = "vcge";


    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(asList(
                new ConcurrentMapCache(METADADOS),
                new ConcurrentMapCache(ORGAOS),
                new ConcurrentMapCache(ORGAO_SIORG),
                new ConcurrentMapCache(VCGE)
        ));
        return manager;
    }

    @Bean
    public KeyGenerator geradorDeChavesParaCacheDeCommitsRecentes() {
        return (target, method, params) -> ((Carta) target).getId();
    }
}
