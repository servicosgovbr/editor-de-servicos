package br.gov.servicos.editor.cartas;

import br.gov.servicos.editor.utils.EscritorDeArquivos;
import br.gov.servicos.editor.utils.LeitorDeArquivos;
import com.github.slugify.Slugify;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import static br.gov.servicos.editor.utils.Unchecked.Supplier.uncheckedSupplier;
import static com.google.common.io.Files.readFirstLine;
import static lombok.AccessLevel.PRIVATE;
import static org.eclipse.jgit.lib.Constants.MASTER;
import static org.eclipse.jgit.lib.Constants.R_HEADS;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PaginaDeOrgao extends ConteudoVersionado<PaginaDeOrgao.Orgao> {

    @Getter
    String id;

    private PaginaDeOrgao(String id, RepositorioGit repositorio, LeitorDeArquivos leitorDeArquivos, EscritorDeArquivos escritorDeArquivos) {
        super(repositorio, leitorDeArquivos, escritorDeArquivos);
        this.id = id;
    }

    @Override
    public Path getCaminho() {
        return Paths.get("conteudo", "orgaos", id + ".md");
    }

    public Orgao getConteudo() {
        File arquivo = getCaminhoAbsoluto().toFile();
        try {
            return getRepositorio().comRepositorioAbertoNoBranch(getBranchRef(),
                    uncheckedSupplier(() -> new Orgao().withNome(readFirstLine(arquivo, Charset.defaultCharset()))));
        } catch (Exception e) {
            return getRepositorio().comRepositorioAbertoNoBranch(R_HEADS + MASTER,
                    uncheckedSupplier(() -> new Orgao().withNome(readFirstLine(arquivo, Charset.defaultCharset()))));
        }
    }

    @Component
    @FieldDefaults(level = PRIVATE, makeFinal = true)
    public static class Formatter implements org.springframework.format.Formatter<PaginaDeOrgao> {
        Factory factory;

        @Autowired
        public Formatter(Factory factory) {
            this.factory = factory;
        }

        @Override
        public PaginaDeOrgao parse(String text, Locale locale) {
            return factory.paginaDeOrgao(text);
        }

        @Override
        public String print(PaginaDeOrgao object, Locale locale) {
            return object.getId();
        }

    }

    @Configuration
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Factory {
        @Autowired
        Slugify slugify;

        @Autowired
        RepositorioGit repositorio;

        @Autowired
        LeitorDeArquivos leitorDeArquivos;

        @Autowired
        EscritorDeArquivos escritorDeArquivos;

        @Bean // necessário para @Cacheable
        @Scope("prototype")
        public PaginaDeOrgao paginaDeOrgao(String texto) {
            return new PaginaDeOrgao(slugify.slugify(texto), repositorio, leitorDeArquivos, escritorDeArquivos);
        }
    }

    @Data
    @Wither
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = PRIVATE)
    public static class Orgao {
        String tipo = "orgao";
        String nome;
    }

}
