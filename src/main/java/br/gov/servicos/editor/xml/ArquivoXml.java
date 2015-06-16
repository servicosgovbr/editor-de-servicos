package br.gov.servicos.editor.xml;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.experimental.FieldDefaults;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.lang.Boolean.parseBoolean;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ArquivoXml {

    private static final String UTF_8 = "UTF-8";

    Optional<Element> xml;

    public ArquivoXml(String caminhoXml, String root) throws IOException {
        this(Jsoup.parse(new File(caminhoXml), UTF_8).select(root).first());
    }

    private ArquivoXml(Element xml) {
        this.xml = ofNullable(xml);
    }

    public String atributo(String atributo) {
        return xml.map(x -> x.attr(atributo).trim()).orElse("");
    }

    public String atributo(String seletor, String atributo) {
        return navega(seletor).atributo(atributo);
    }

    public Boolean atrituboAtivo(String atributo) {
        return paraBooleano(atributo(atributo));
    }

    public Boolean textoAtivo(String seletor) {
        return paraBooleano(navega(seletor).texto());
    }

    public String texto() {
        return xml.map(x -> x.text().trim()).orElse("");
    }

    public String texto(String seletor) {
        return navega(seletor).texto();
    }

    public List<String> coleta(String seletor) {
        return coleta(seletor, ArquivoXml::texto);
    }

    public <T> List<T> coleta(String seletor, Function<ArquivoXml, T> conversor) {
        return xml.map(
                x -> x.select(seletor)
                        .stream()
                        .map(e -> new ArquivoXml(e).converte(conversor))
                        .collect(toList())
        ).orElse(emptyList());
    }

    public <T> T converte(Function<ArquivoXml, T> conversor) {
        return conversor.apply(this);
    }

    public <T> T converte(String seletor, Function<ArquivoXml, T> conversor) {
        return navega(seletor).converte(conversor);
    }

    private ArquivoXml navega(String seletor) {
        return new ArquivoXml(
                xml.map(x -> x.select(seletor).first()).orElse(null)
        );
    }

    @SuppressFBWarnings(value = "NP_BOOLEAN_RETURN_NULL", justification = "Campos não preenchidos não devem ser false")
    private Boolean paraBooleano(String valor) {
        if (valor.equals("true") || valor.equals("false"))
            return parseBoolean(valor);

        return null;
    }

}
