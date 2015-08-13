package br.gov.servicos.editor.utils;

import org.junit.Test;

import java.io.File;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LeitorDeArquivosTest {

    @Test
    public void leArquivoCorretamente() throws Exception {
        assertThat(new LeitorDeArquivos().ler(new File("README.md")).isPresent(), is(true));
    }

    @Test
    public void retornaVazioAoTentarLerArquivoInexistente() throws Exception {
        assertThat(new LeitorDeArquivos().ler(new File("fail")).isPresent(), is(false));
    }

    @Test
    public void retornaVazioAoTentarLerDiretorio() throws Exception {
        assertThat(new LeitorDeArquivos().ler(new File(".")).isPresent(), is(false));
    }
}