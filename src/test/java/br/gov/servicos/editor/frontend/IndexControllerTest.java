package br.gov.servicos.editor.frontend;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class IndexControllerTest {

    @Test
    public void retornaIndex() throws Exception {
        assertThat(new IndexController().index().getViewName(), is("index"));
    }
}