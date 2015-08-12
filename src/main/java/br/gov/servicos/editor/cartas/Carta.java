package br.gov.servicos.editor.cartas;

import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.eclipse.jgit.transport.RefSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import static lombok.AccessLevel.PRIVATE;
import static org.eclipse.jgit.lib.Constants.R_HEADS;

@FieldDefaults(level = PRIVATE, makeFinal = true)
public class Carta {

    @Configuration
    public static class Factory {

        @Bean
        public Formatter<Carta> cartaFormatter(File raiz) {
            return new Formatter<Carta>() {
                @Override
                public Carta parse(String text, Locale locale) {
                    return new Carta(text, raiz);
                }

                @Override
                public String print(Carta object, Locale locale) {
                    return object.toString();
                }
            };
        }
    }

    @Getter
    String id;

    File raiz;

    private Carta(String id, File raiz) {
        this.id = id;
        this.raiz = raiz;
    }

    public Path caminhoAbsoluto() {
        return Paths.get(raiz.getAbsolutePath(), "cartas-servico", "v3", "servicos", id + ".xml");
    }

    public RefSpec getRefSpec() {
        return new RefSpec(id + ":" + id);
    }


    public String getBranchRef() {
        return R_HEADS + id;
    }

    public Path caminhoRelativo() {
        return raiz.toPath().relativize(caminhoAbsoluto());
    }
}
