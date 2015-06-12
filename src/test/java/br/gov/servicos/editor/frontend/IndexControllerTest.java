package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.servicos.Servico;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class IndexControllerTest {

    @Test
    public void retornaIndex() throws Exception {
        assertThat(new IndexController().index().getViewName(), is("index"));
    }

    @Test
    public void adicionaServicoVazioAoModel() throws Exception {
        assertThat(new IndexController().index().getModelMap().get("servico"), is(new Servico()));
    }
}