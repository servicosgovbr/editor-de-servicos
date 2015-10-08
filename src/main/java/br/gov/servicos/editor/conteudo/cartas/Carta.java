package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.git.RepositorioGit;
import br.gov.servicos.editor.utils.EscritorDeArquivos;
import br.gov.servicos.editor.utils.LeitorDeArquivos;
import br.gov.servicos.editor.utils.ReformatadorXml;
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

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import static javax.xml.bind.JAXB.unmarshal;
import static javax.xml.bind.annotation.XmlAccessType.NONE;
import static lombok.AccessLevel.PRIVATE;
import static org.eclipse.jgit.lib.Constants.MASTER;
import static org.eclipse.jgit.lib.Constants.R_HEADS;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class Carta extends ConteudoVersionado<Carta.Servico> {

    @Getter
    String id;

    private Carta(String id, RepositorioGit repositorio, LeitorDeArquivos leitorDeArquivos, EscritorDeArquivos escritorDeArquivos, Slugify slugify, ReformatadorXml reformatadorXml) {
        super(repositorio, leitorDeArquivos, escritorDeArquivos, slugify, reformatadorXml);
        this.id = id;
    }

    @Override
    public Path getCaminho() {
        return Paths.get("cartas-servico", "v3", "servicos", id + ".xml");
    }

    public Servico getConteudo() {
        File arquivo = getCaminhoAbsoluto().toFile();
        try {
            return getRepositorio().comRepositorioAbertoNoBranch(getBranchRef(),
                    () -> unmarshal(arquivo, Servico.class));
        } catch (Exception e) {
            return getRepositorio().comRepositorioAbertoNoBranch(R_HEADS + MASTER,
                    () -> unmarshal(arquivo, Servico.class));
        }
    }

    @Component
    @FieldDefaults(level = PRIVATE, makeFinal = true)
    public static class Formatter implements org.springframework.format.Formatter<Carta> {
        Factory factory;

        @Autowired
        public Formatter(Factory factory) {
            this.factory = factory;
        }

        @Override
        public Carta parse(String text, Locale locale) {
            return factory.carta(text);
        }

        @Override
        public String print(Carta object, Locale locale) {
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
        public Carta carta(String texto) {
            return new Carta(slugify.slugify(texto), repositorio, leitorDeArquivos, escritorDeArquivos, slugify, reformatadorXml);
        }
    }

    @Data
    @Wither
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = PRIVATE)
    @XmlAccessorType(NONE)
    @XmlType(name = "Servico", propOrder = {"nome", "orgao"})
    public static class Servico {
        String tipo = "servico";

        @XmlElement(required = true)
        String nome;

        @XmlElement(required = true)
        Orgao orgao;
    }

    @Data
    @Wither
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = PRIVATE)
    @XmlAccessorType(NONE)
    @XmlType(name = "Orgao", propOrder = {"id"})
    public static class Orgao {
        @XmlAttribute(required = true)
        String id;
    }
}
