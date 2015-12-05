package br.gov.servicos.editor.security;

import lombok.Getter;

public enum TipoPermissao {
    SALVAR("SALVAR"),
    PUBLICAR("PUBLICAR"),
    DESPUBLICAR("DESPUBLICAR"),
    DESCARTAR("DESCARTAR"),
    EXCLUIR("EXCLUIR"),
    RENOMEAR("RENOMEAR"),
    CADASTRAR("CADASTRAR"),
    CADASTRAR_COM_OUTRO_ORGAO("CADASTRAR_COM_OUTRO_ORGAO"),
    CADASTRAR_ADMIN("CADASTRAR_ADMIN"),
    CADASTRAR_PONTO_FOCAL("CADASTRAR_PONTO_FOCAL"),
    CADASTRAR_PUBLICADOR("CADASTRAR_PUBLICADOR"),
    CADASTRAR_EDITOR("CADASTRAR_EDITOR");

    private static final String ORGAO_ESPECIFICO = " (ORGAO ESPECIFICO)";

    @Getter
    private final String nome;

    TipoPermissao(String nome) {
        this.nome = nome;
    }

    public String comOrgaoEspecifico() {
        return nome + ORGAO_ESPECIFICO;
    }
}
