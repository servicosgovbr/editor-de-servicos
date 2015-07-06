package br.gov.servicos.editor.editar;

import br.gov.servicos.editor.servicos.Etapa;
import br.gov.servicos.editor.servicos.Servico;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;

public class EditarEtapasControllerTest {

    EditarEtapasController controller;

    @Before
    public void setUp() throws Exception {
        controller = new EditarEtapasController();
    }

    @Test
    public void deveAdicionarEtapas() throws Exception {
        Servico antes = new Servico().withEtapas(new ArrayList<>(asList(new Etapa().withTitulo("Etapa 1"))));
        Servico depois = new Servico().withEtapas(asList(new Etapa().withTitulo("Etapa 1"), new Etapa()));

        ModelAndView mav = controller.adicionarEtapa(antes);
        assertModelAttributeValue(mav, "servico", depois);
    }

}
