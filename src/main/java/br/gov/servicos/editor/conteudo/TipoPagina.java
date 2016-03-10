package br.gov.servicos.editor.conteudo;

import br.gov.servicos.editor.config.SlugifyConfig;
import lombok.Getter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public enum TipoPagina {
    ORGAO("conteudo/orgaos", "xml"),
    PAGINA_TEMATICA("conteudo/paginas-tematicas", "xml"),
    SERVICO("cartas-servico/v3/servicos", "xml");

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
}
