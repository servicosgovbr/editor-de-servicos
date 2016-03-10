package br.gov.servicos.editor.utils;


import br.gov.servicos.editor.conteudo.DeserializadorConteudoXML;
import br.gov.servicos.editor.conteudo.OrgaoXML;
import br.gov.servicos.editor.conteudo.PaginaTematicaXML;
import br.gov.servicos.editor.conteudo.TipoPagina;
import br.gov.servicos.editor.conteudo.cartas.ServicoXML;

public class Deserializador {

    public static DeserializadorConteudoXML para(TipoPagina tipo) {
        switch (tipo) {
            case SERVICO:
                return new DeserializadorConteudoXML(ServicoXML.class);
            case ORGAO:
                return new DeserializadorConteudoXML(OrgaoXML.class);
            case PAGINA_TEMATICA:
                return new DeserializadorConteudoXML(PaginaTematicaXML.class);
        }
        throw new IllegalArgumentException("Tipo inexistente: " + tipo);
    }
}
