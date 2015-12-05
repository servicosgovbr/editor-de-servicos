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
    CADASTRAR_OUTROS_ORGAOS("CADASTRAR OUTROS ORGAOS");

    public static final String CADASTRAR_PREFIXO = "CADASTRAR ";
    private static final String ORGAO_ESPECIFICO = " (ORGAO ESPECIFICO)";

    @Getter
    private final String nome;

    TipoPermissao(String nome) {
        this.nome = nome;
    }

    public String comOrgaoEspecifico() {
        return nome + ORGAO_ESPECIFICO;
    }

    public String comPapel(String papel) {
        return nome + " " + papel.toUpperCase();
    }
}
