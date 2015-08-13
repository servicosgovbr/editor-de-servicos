package br.gov.servicos.editor.frontend;

import lombok.experimental.FieldDefaults;
import org.junit.Before;
import org.junit.Test;

import static lombok.AccessLevel.PRIVATE;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@FieldDefaults(level = PRIVATE)
public class IndexControllerTest {

    IndexController controller;

    @Before
    public void setUp() throws Exception {
        controller = new IndexController();
    }

    @Test
    public void redirecionaParaIndex() throws Exception {
        assertThat(controller.root().getUrl(), is("/editar/"));
    }

    @Test
    public void retornaIndex() throws Exception {
        assertThat(controller.index().getViewName(), is("index"));
    }

    @Test
    public void retornaAjudaComMarkdown() throws Exception {
        assertThat(controller.ajudaComMarkdown().getViewName(), is("ajuda-markdown"));
    }
}