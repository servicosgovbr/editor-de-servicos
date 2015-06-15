package br.gov.servicos.editor.config;

import br.gov.servicos.editor.servicos.Orgao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.text.ParseException;
import java.util.Locale;

@Configuration
@EnableWebMvc
public class ConversoresConfig extends WebMvcConfigurerAdapter {

    @Autowired
    ConteudoDeReferencia referencia;

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
