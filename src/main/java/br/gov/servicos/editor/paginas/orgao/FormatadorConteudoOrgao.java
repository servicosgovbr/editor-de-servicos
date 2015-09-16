package br.gov.servicos.editor.paginas.orgao;

import br.gov.servicos.editor.cartas.PaginaDeOrgao;
import org.springframework.stereotype.Component;

@Component
public class FormatadorConteudoOrgao {

    public String formatar(PaginaDeOrgao.Orgao orgao) {
        return new StringBuilder()
                .append(orgao.getNome())
                .append(System.lineSeparator())
                .append("---")
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(orgao.getConteudo())
                .toString();
    }

}
