package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.servicos.Cartas;
import br.gov.servicos.editor.servicos.Metadados;
import br.gov.servicos.editor.servicos.Servico;
import lombok.experimental.FieldDefaults;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import static lombok.AccessLevel.PRIVATE;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
@FieldDefaults(level = PRIVATE)
public class IndexControllerTest {

    @Autowired
    ExportadorServicoV2 v2;

    @Autowired
    Cartas cartas;

    IndexController controller;

    @Before
    public void setUp() throws Exception {
        controller = new IndexController();
    }

    @Test
    public void retornaIndex() throws Exception {
        assertThat(controller.index().getViewName(), is("index"));
    }

    @Test
    public void adicionaServicoVazioAoModel() throws Exception {
        assertThat(controller.index().getModelMap().get("servico"), is(
                new Servico().withMetadados(new Metadados())));
    }

}