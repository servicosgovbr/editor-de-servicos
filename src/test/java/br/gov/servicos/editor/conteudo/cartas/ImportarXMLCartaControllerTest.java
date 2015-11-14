package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.utils.EscritorDeArquivos;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.XmlExpectationsHelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImportarXMLCartaControllerTest {

    private Path valido;
    private Path xml_inexistente;

    @Before
    public void setup() throws IOException {
        valido = Files.createTempFile("valido", "xml");
        valido.toFile().deleteOnExit();
        xml_inexistente = Files.createTempFile("xml_inexistente", "xml");
        xml_inexistente.toFile().delete();

        new EscritorDeArquivos().escrever(valido, "<servico><nome>Carta A</nome></servico>");
    }

    @Test
    public void deveReceberUrlDeXmlERetornarConteudoDeXml() throws Exception {
        String xml = new ImportarXMLCartaController().editar(valido.toUri().toString());
        new XmlExpectationsHelper().assertXmlEqual("<servico><nome>Carta A</nome></servico>", xml);
    }

    @Test(expected = FileNotFoundException.class)
    public void deveReceberUrlDeXmlEValidarFormatoXml() throws Exception {
        new ImportarXMLCartaController().editar("https://urlquenaoexiste/arquivo.xml");
    }

}