package br.gov.servicos.editor.conteudo;

import br.gov.servicos.editor.config.SlugifyConfig;
import br.gov.servicos.editor.conteudo.cartas.ServicoXML;
import br.gov.servicos.editor.frontend.Siorg;
import br.gov.servicos.editor.git.ConteudoMetadados;
import lombok.Getter;

import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static javax.xml.bind.JAXB.unmarshal;

public enum TipoPagina {
    ORGAO("conteudo/orgaos", "xml") {
        @Override
        public ConteudoMetadados metadados(String conteudo, Siorg siorg) {
            return conteudoMetadados(conteudo, siorg, OrgaoXML.class);
        }
    },

    PAGINA_TEMATICA("conteudo/paginas-tematicas", "xml") {
        @Override
        public ConteudoMetadados metadados(String conteudo, Siorg siorg) {
            return conteudoMetadados(conteudo, siorg, PaginaTematicaXML.class);
        }
    },

    SERVICO("cartas-servico/v3/servicos", "xml") {
        @Override
        public ConteudoMetadados metadados(String conteudo, Siorg siorg) {
            return conteudoMetadados(conteudo, siorg, ServicoXML.class);
        }
    };

    private static ConteudoMetadados conteudoMetadados(String conteudo, Siorg siorg, Class<? extends ConteudoMetadadosProvider> type) {
        return unmarshal(new StreamSource(new StringReader(conteudo)), type)
                .toConteudoMetadados(siorg);
    }

    @Getter
    private final String nome;

    @Getter
    private final Path caminhoPasta;

    @Getter
    private final String extensao;

    TipoPagina(String caminhoPasta, String extensao) {
        this.nome = SlugifyConfig.slugify(name());
        this.caminhoPasta = Paths.get(caminhoPasta);
        this.extensao = extensao;
    }

    @Override
    public String toString() {
        return nome;
    }

    public static TipoPagina fromNome(String nome) {
        return Arrays.asList(values())
                .stream()
                .filter(t -> t.getNome().equals(nome))
                .findFirst()
                .get();
    }

    public String prefixo() {
        return getNome() + '-';
    }

    public abstract ConteudoMetadados metadados(String conteudo, Siorg siorg);
}
