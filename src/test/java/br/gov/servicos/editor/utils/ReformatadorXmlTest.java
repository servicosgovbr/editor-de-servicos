package br.gov.servicos.editor.utils;

import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.dom.TextImpl;
import org.junit.Before;
import org.junit.Test;

import javax.xml.transform.dom.DOMSource;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ReformatadorXmlTest {

    ReformatadorXml xml;

    @Before
    public void setUp() throws Exception {
        xml = new ReformatadorXml();
    }

    @Test
    public void reformataXmlVazio() throws Exception {
        assertThat(xml.formata(new DOMSource()),
                is("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n\n"));
    }

    @Test
    public void reformataXml() throws Exception {
        DocumentImpl d = new DocumentImpl();
        ElementImpl e = new ElementImpl(d, "servico");
        e.appendChild(new TextImpl(d, "\n\nteste\n\tteste"));
        d.appendChild(e);

        assertThat(xml.formata(new DOMSource(d)),
                is("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<servico>\n\nteste\n\tteste</servico>\n"));
    }
}