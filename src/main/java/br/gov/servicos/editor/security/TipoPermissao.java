package br.gov.servicos.editor.security;

import br.gov.servicos.editor.conteudo.TipoPagina;
import lombok.Getter;

public enum TipoPermissao {
    CRIAR("CRIAR"),
    EDITAR_SALVAR("EDITAR_SALVAR"),
    PUBLICAR("PUBLICAR"),
    DESPUBLICAR("DESPUBLICAR"),
    DESCARTAR("DESCARTAR"),
    EXCLUIR("EXCLUIR"),
    CADASTRAR("CADASTRAR"),
    CADASTRAR_OUTROS_ORGAOS("CADASTRAR OUTROS ORGAOS");

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

    public String comTipoPagina(TipoPagina tipoPagina) {
        return nome + " " + tipoPagina.toString().toUpperCase();
    }

    public String comTipoPaginaParaOrgaoEspecifico(TipoPagina tipoPagina) {
        return comTipoPagina(tipoPagina) + ORGAO_ESPECIFICO;
    }
}
