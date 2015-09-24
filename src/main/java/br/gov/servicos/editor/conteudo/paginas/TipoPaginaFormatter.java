package br.gov.servicos.editor.conteudo.paginas;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class TipoPaginaFormatter implements Formatter<TipoPagina> {
    @Override
    public TipoPagina parse(String text, Locale locale) throws ParseException {
        return TipoPagina.fromNome(text);
    }

    @Override
    public String print(TipoPagina object, Locale locale) {
        return object.name();
    }
}
