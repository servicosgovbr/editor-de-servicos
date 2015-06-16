package br.gov.servicos.editor.config;

import br.gov.servicos.editor.servicos.Orgao;
import com.github.slugify.Slugify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Locale;

@Configuration
public class ConversoresConfig extends WebMvcConfigurerAdapter {

    @Autowired
    ConteudoDeReferencia referencia;

    @Bean
    Slugify slugify() throws IOException {
        return new Slugify(true);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        super.addFormatters(registry);
        registry.addFormatter(new Formatter<Orgao>() {
            @Override
            public Orgao parse(String text, Locale locale) throws ParseException {
                return referencia.orgaos().stream().filter(o -> o.getId().equals(text)).findFirst().orElse(null);
            }

            @Override
            public String print(Orgao object, Locale locale) {
                return object.getId();
            }
        });
    }
}
