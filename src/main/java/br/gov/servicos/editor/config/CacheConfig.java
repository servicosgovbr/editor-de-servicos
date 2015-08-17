package br.gov.servicos.editor.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Arrays.asList;
import static org.eclipse.jgit.lib.Constants.R_HEADS;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String COMMITS_RECENTES = "commits-recentes";
    public static final String ORGAOS = "orgaos";

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(asList(
                new ConcurrentMapCache(COMMITS_RECENTES),
                new ConcurrentMapCache(ORGAOS)
        ));
        return manager;
    }

    @Bean
    public KeyGenerator chavesParaCommitMaisRecenteDoArquivo() {
        return (target, method, params) -> params[0].toString().replaceAll("cartas-servico/v3/servicos/(.*)\\.xml", "$1");
    }

    @Bean
    public KeyGenerator chavesParaCommitMaisRecenteDoBranch() {
        return (target, method, params) -> params[0].toString().replaceAll("^" + R_HEADS, "");
    }

}
