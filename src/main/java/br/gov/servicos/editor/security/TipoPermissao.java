package br.gov.servicos.editor.security;

import lombok.Getter;

public enum TipoPermissao {
    PUBLICAR("publicar"),
    SALVAR("salvar"),
    DESPUBLICAR("despublicar"),
    DESCARTAR("descartar"),
    EXCLUIR("excluir"),
    RENOMEAR("renomear"),
    CADASTRAR("cadastrar");

    @Getter
    private final String nome;

    TipoPermissao(String nome) {
        this.nome = nome;
    }
}
