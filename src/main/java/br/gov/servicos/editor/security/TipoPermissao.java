package br.gov.servicos.editor.security;

import lombok.Getter;

public enum TipoPermissao {
    PUBLICAR("PUBLICAR"),
    SALVAR("SALVAR"),
    DESPUBLICAR("DESPUBLICAR"),
    DESCARTAR("DESCARTAR"),
    EXCLUIR("EXCLUIR"),
    RENOMEAR("RENOMEAR"),
    CADASTRAR("CADASTRAR");

    @Getter
    private final String nome;

    TipoPermissao(String nome) {
        this.nome = nome;
    }
}
