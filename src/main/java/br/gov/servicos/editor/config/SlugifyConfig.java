package br.gov.servicos.editor.config;

import com.github.slugify.Slugify;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SlugifyConfig {

    @Bean
    public Slugify slugify() throws IOException {
        return new Slugify();
    }
}
