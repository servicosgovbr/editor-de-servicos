package br.gov.servicos.editor.conteudo.paginas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.RepositorioGit;
import br.gov.servicos.editor.utils.EscritorDeArquivos;
import br.gov.servicos.editor.utils.LeitorDeArquivos;
import br.gov.servicos.editor.utils.ReformatadorXml;
import com.github.slugify.Slugify;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
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
public class PaginaVersionada extends ConteudoVersionado<Pagina> {

    @Getter
    String id;

    private PaginaVersionada(String id, RepositorioGit repositorio, LeitorDeArquivos leitorDeArquivos, EscritorDeArquivos escritorDeArquivos, Slugify slugify, ReformatadorXml reformatadorXml) {
        super(repositorio, leitorDeArquivos, escritorDeArquivos, slugify, reformatadorXml);
        this.id = id;
    }

    @Override
    public Path getCaminho() {
        return Paths.get("conteudo", "orgaos", id + ".md");
    }

    public Pagina getConteudo() {
        File arquivo = getCaminhoAbsoluto().toFile();
        try {
            return getRepositorio().comRepositorioAbertoNoBranch(getBranchRef(),
                    uncheckedSupplier(() -> new Pagina().withNome(readFirstLine(arquivo, Charset.defaultCharset()))));
        } catch (Exception e) {
            return getRepositorio().comRepositorioAbertoNoBranch(R_HEADS + MASTER,
                    uncheckedSupplier(() -> new Pagina().withNome(readFirstLine(arquivo, Charset.defaultCharset()))));
        }
    }

    @Component
    @FieldDefaults(level = PRIVATE, makeFinal = true)
    public static class Formatter implements org.springframework.format.Formatter<PaginaVersionada> {
        Factory factory;

        @Autowired
        public Formatter(Factory factory) {
            this.factory = factory;
        }

        @Override
        public PaginaVersionada parse(String text, Locale locale) {
            return factory.paginaDeOrgao(text);
        }

        @Override
        public String print(PaginaVersionada object, Locale locale) {
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

        @Autowired
        ReformatadorXml reformatadorXml;

        @Bean // necessário para @Cacheable
        @Scope("prototype")
        public PaginaVersionada paginaDeOrgao(String texto) {
            return new PaginaVersionada(slugify.slugify(texto), repositorio, leitorDeArquivos, escritorDeArquivos, slugify, reformatadorXml);
        }
    }

}
