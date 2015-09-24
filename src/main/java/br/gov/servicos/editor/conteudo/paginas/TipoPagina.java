package br.gov.servicos.editor.conteudo.paginas;

import lombok.Getter;

import java.util.Arrays;

public enum TipoPagina {
    ORGAO("orgao", "orgaos"),
    AREA_INTERESSE("area-de-interesse", "areas-de-interesse"),
    ESPECIAL("especial", "paginas-especiais");

    @Getter
    private String nome;

    @Getter
    private String nomePasta;

    TipoPagina(String nome, String nomePasta) {
        this.nome = nome;
        this.nomePasta = nomePasta;
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
}
