package br.gov.servicos.editor.editar;

import br.gov.servicos.editor.servicos.Servico;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;

public class EditarSolicitantesControllerTest {

    EditarSolicitantesController controller;

    @Before
    public void setUp() throws Exception {
        controller = new EditarSolicitantesController();
    }

    @Test
    public void deveAdicionarSolicitante() throws Exception {
        Servico antes = new Servico().withSolicitantes(new ArrayList<>(asList("primeiro", "segundo")));
        Servico depois = antes.withSolicitantes(new ArrayList<>(asList("primeiro", "segundo", "")));

        ModelAndView mav = controller.adicionarSolicitante(antes);

        assertModelAttributeValue(mav, "servico", depois);
    }

    @Test
    public void deveRemoverSolicitante() throws Exception {
        Servico antes = new Servico().withSolicitantes(new ArrayList<>(asList("primeiro", "segundo")));
        Servico depois = antes.withSolicitantes(new ArrayList<>(asList("primeiro")));

        ModelAndView mav = controller.removerSolicitante(antes, 1);

        assertModelAttributeValue(mav, "servico", depois);
    }
}