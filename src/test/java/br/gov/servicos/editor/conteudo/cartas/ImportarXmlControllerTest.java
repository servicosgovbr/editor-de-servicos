package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.utils.EscritorDeArquivos;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.XmlExpectationsHelper;

import java.nio.file.Files;
import java.nio.file.Path;

public class ImportarXmlControllerTest {

    private Path valido;

    @Before
    public void setup() throws Exception {
        valido = Files.createTempFile("valido", "xml");
        valido.toFile().deleteOnExit();

        new EscritorDeArquivos().escrever(valido, "<servico><nome>Carta A</nome></servico>");
    }

    @Test
    public void deveReceberUrlDeXmlERetornarConteudoDeXml() throws Exception {
        String xml = new ImportarXmlController().editar(valido.toUri().toString());
        new XmlExpectationsHelper().assertXmlEqual("<servico><nome>Carta A</nome></servico>", xml);
    }

    @Test(expected = Exception.class)
    public void deveReceberUrlDeXmlEValidarFormatoXml() throws Exception {
        new ImportarXmlController().editar("https://urlquenaoexiste.com/arquivo.xml");
    }

}