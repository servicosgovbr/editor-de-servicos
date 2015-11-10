package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.frontend.Siorg;
import br.gov.servicos.editor.git.Metadados;
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

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

import static br.gov.servicos.editor.conteudo.paginas.TipoPagina.SERVICO;
import static javax.xml.bind.JAXB.unmarshal;
import static javax.xml.bind.annotation.XmlAccessType.NONE;
import static lombok.AccessLevel.PRIVATE;
import static org.eclipse.jgit.lib.Constants.MASTER;
import static org.eclipse.jgit.lib.Constants.R_HEADS;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class Carta extends ConteudoVersionado<Carta.Servico> {

    Siorg siorg;

    public Carta(String id, RepositorioGit repositorio, LeitorDeArquivos leitorDeArquivos, EscritorDeArquivos escritorDeArquivos, Slugify slugify, ReformatadorXml reformatadorXml, Siorg siorg) {
        super(id, repositorio, SERVICO, leitorDeArquivos, escritorDeArquivos, slugify, reformatadorXml);
        this.siorg = siorg;
    }

    @Override
    public Path getCaminho() {
        return getTipo().getCaminhoPasta().resolve(getId() + ".xml");
    }

    @Override
    protected Metadados<Servico> internalGetMetadados() {
        return super.internalGetMetadados()
                .withNomeOrgao(nomeOrgao());
    }

    private String nomeOrgao() {
        return Optional.ofNullable(getMetadadosConteudo())
                .map(Servico::getOrgao)
                .map(Orgao::getId)
                .map(siorg::nomeDoOrgao)
                .map(o -> o.orElse(null))
                .orElse(" - - ");
    }

    public Servico getMetadadosConteudo() {
        File arquivo = getCaminhoAbsoluto().toFile();
        try {
            return getRepositorio().comRepositorioAbertoNoBranch(getBranchRef(),
                    () -> unmarshal(arquivo, Servico.class));
        } catch (Exception e) {
            return getRepositorio().comRepositorioAbertoNoBranch(R_HEADS + MASTER,
                    () -> unmarshal(arquivo, Servico.class));
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
