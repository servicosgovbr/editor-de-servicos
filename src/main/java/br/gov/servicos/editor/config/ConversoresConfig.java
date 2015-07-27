package br.gov.servicos.editor.config;

import com.github.slugify.Slugify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;

@Configuration
public class ConversoresConfig extends WebMvcConfigurerAdapter {

    @Autowired
    ConteudoDeReferencia referencia;

    @Bean
    Slugify slugify() throws IOException {
        return new Slugify(true);
    }
}
