package br.gov.servicos.editor.utils;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class EscritorDeArquivosTest {

    private EscritorDeArquivos escritor;

    @Before
    public void setUp() throws Exception {
        escritor = new EscritorDeArquivos();
    }

    @Test
    public void escreveArquivosCorretamente() throws Exception {
        Path caminho = Files.createTempFile("escritor-de-arquivos", "teste");

        escritor.escrever(caminho, "conteúdo\n");

        assertThat(Files.readAllLines(caminho).get(0), is("conteúdo"));
    }
}