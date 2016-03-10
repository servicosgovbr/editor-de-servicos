package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;

import static java.lang.String.format;

public class ConteudoInexistenteException extends RuntimeException {

    public ConteudoInexistenteException(ConteudoVersionado conteudo) {
        super(format("%s %s não encontrado em: %s", conteudo.getTipo().getNome(), conteudo.getId(), conteudo.getCaminhoAbsoluto()));
    }

}
