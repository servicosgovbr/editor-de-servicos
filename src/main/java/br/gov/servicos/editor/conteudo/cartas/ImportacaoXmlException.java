package br.gov.servicos.editor.conteudo.cartas;

public class ImportacaoXmlException extends Exception {

    public ImportacaoXmlException(String url, Exception e) {
        super("Problemas ao importar XML a partir da url: " + url + "\n Detalhes: " + e.getMessage());
    }

}
