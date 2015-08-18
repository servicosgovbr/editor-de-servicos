package br.gov.servicos.editor.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.Arrays.asList;

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

}
