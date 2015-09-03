package br.gov.servicos.editor.cartas;

import br.gov.servicos.editor.servicos.Metadados;
import br.gov.servicos.editor.servicos.Revisao;
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
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Optional;

import static br.gov.servicos.editor.config.CacheConfig.METADADOS;
import static java.lang.String.format;
import static javax.xml.bind.JAXB.unmarshal;
import static javax.xml.bind.annotation.XmlAccessType.NONE;
import static lombok.AccessLevel.PRIVATE;
import static org.eclipse.jgit.lib.Constants.MASTER;
import static org.eclipse.jgit.lib.Constants.R_HEADS;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
@CacheConfig(cacheNames = METADADOS, keyGenerator = "geradorDeChavesParaCacheDeCommitsRecentes")
public class Carta {

    @Getter
    String id;

    RepositorioGit repositorio;
    LeitorDeArquivos leitorDeArquivos;
    EscritorDeArquivos escritorDeArquivos;

    private Carta(String id, RepositorioGit repositorio, LeitorDeArquivos leitorDeArquivos, EscritorDeArquivos escritorDeArquivos) {
        this.id = id;
        this.repositorio = repositorio;
        this.leitorDeArquivos = leitorDeArquivos;
        this.escritorDeArquivos = escritorDeArquivos;
    }

    public Path getCaminhoAbsoluto() {
        return repositorio.getCaminhoAbsoluto().resolve(Paths.get("cartas-servico", "v3", "servicos", id + ".xml")).toAbsolutePath();
    }

    public String getBranchRef() {
        return R_HEADS + id;
    }

    public Path getCaminhoRelativo() {
        return repositorio.getCaminhoAbsoluto().relativize(getCaminhoAbsoluto());
    }

    @Cacheable
    public Metadados getMetadados() {
        Optional<Revisao> master = repositorio.getRevisaoMaisRecenteDoArquivo(getCaminhoRelativo());
        Optional<Revisao> branch = repositorio.getRevisaoMaisRecenteDoBranch(getBranchRef());

        return new Metadados()
                .withId(id)
                .withPublicado(master.orElse(null))
                .withEditado(branch.orElse(null))
                .withServico(getConteudo());
    }

    public String getConteudoRaw() throws FileNotFoundException {
        return repositorio.comRepositorioAbertoNoBranch(getBranchRef(),
                () -> leitorDeArquivos.ler(getCaminhoAbsoluto().toFile())
        ).orElseThrow(
                () -> new FileNotFoundException("Não foi possível encontrar o serviço referente ao arquivo '" + getId() + "'")
        );
    }

    public Servico getConteudo() {
        File arquivo = getCaminhoAbsoluto().toFile();
        try {
            return repositorio.comRepositorioAbertoNoBranch(getBranchRef(),
                    () -> unmarshal(arquivo, Servico.class));
        } catch (Exception e) {
            return repositorio.comRepositorioAbertoNoBranch(R_HEADS + MASTER,
                    () -> unmarshal(arquivo, Servico.class));
        }
    }

    @CacheEvict
    public void salvar(User usuario, String conteudo) {
        repositorio.comRepositorioAbertoNoBranch(getBranchRef(), () -> {
            repositorio.pull();

            try {
                escritorDeArquivos.escrever(getCaminhoAbsoluto(), conteudo);

                repositorio.add(getCaminhoRelativo());

                String mensagem = format("%s '%s'", getCaminhoAbsoluto().toFile().exists() ? "Altera" : "Cria", getId());
                repositorio.commit(getCaminhoRelativo(), mensagem, usuario);

            } finally {
                repositorio.push(getBranchRef());
            }

            return null;
        });
    }

    @CacheEvict
    public void remover(User usuario) {
        repositorio.comRepositorioAbertoNoBranch(this.getBranchRef(), () -> {
            repositorio.pull();

            try {
                repositorio.remove(getCaminhoRelativo());
                repositorio.commit(getCaminhoRelativo(), "Remove '" + id + "'", usuario);
            } finally {
                repositorio.push(getBranchRef());
            }

            return null;
        });
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

        @Bean // necessário para @Cacheable
        @Scope("prototype")
        public Carta carta(String texto) {
            return new Carta(slugify.slugify(texto), repositorio, leitorDeArquivos, escritorDeArquivos);
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
