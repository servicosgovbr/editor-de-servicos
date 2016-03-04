package br.gov.servicos.editor.conteudo;

import lombok.Value;

import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

import static javax.xml.bind.JAXB.unmarshal;

@Value
public class DeserializadorConteudoXML {

    Class<? extends ConteudoMetadadosProvider> clazz;

    public ConteudoMetadadosProvider deserializaConteudo(String raw) {
        return unmarshal(new StreamSource(new StringReader(raw)), clazz);
    }
}
