package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;

public class ConteudoInexistenteException extends Exception {

    public ConteudoInexistenteException(ConteudoVersionado conteudo) {
        super(conteudo.getTipo().getNome() + " " + conteudo.getId() + " não encontrado em: " + conteudo.getCaminhoAbsoluto());
    }

}
