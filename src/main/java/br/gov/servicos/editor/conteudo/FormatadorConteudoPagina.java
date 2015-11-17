package br.gov.servicos.editor.conteudo;

import org.springframework.stereotype.Component;

@Component
public class FormatadorConteudoPagina {

    public String formatar(Pagina pagina) {
        return new StringBuilder()
                .append(pagina.getNome())
                .append(System.lineSeparator())
                .append("---")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(pagina.getConteudo())
                .toString();
    }

}
