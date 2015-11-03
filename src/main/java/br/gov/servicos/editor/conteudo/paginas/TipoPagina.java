package br.gov.servicos.editor.conteudo.paginas;

import br.gov.servicos.editor.config.SlugifyConfig;
import lombok.Getter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public enum TipoPagina {
    ORGAO("conteudo/orgaos", "md"),
    AREA_DE_INTERESSE("conteudo/areas-de-interesse", "md"),
    PAGINA_ESPECIAL("conteudo/paginas-especiais", "md"),
    SERVICO("cartas-servico/v3/servicos", "xml");

    @Getter
    private String nome;

    @Getter
    private Path caminhoPasta;

    @Getter
    private String extensao;

    TipoPagina(String caminhoPasta, String extensao) {
        this.nome = SlugifyConfig.slugify(name());
        this.caminhoPasta = Paths.get(caminhoPasta);
        this.extensao = extensao;
    }

    @Override
    public String toString() {
        return this.nome;
    }

    public static TipoPagina fromNome(String nome) {
        return Arrays.asList(values())
                .stream()
                .filter(t -> t.getNome().equals(nome))
                .findFirst()
                .get();
    }

    public String prefixo() {
        return getNome() + "-";
    }

}
