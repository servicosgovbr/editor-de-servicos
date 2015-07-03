package br.gov.servicos.editor.editar;

import br.gov.servicos.editor.servicos.Servico;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;

@RunWith(MockitoJUnitRunner.class)
public class EditarControllerTest {

    private EditarController controller;

    @Before
    public void setUp() throws Exception {
        controller = new EditarController(null, null, null);
    }

    @Test
    public void deveAdicionarSolicitante() throws Exception {
        Servico antes = new Servico().withSolicitantes(new ArrayList<>(asList("primeiro", "segundo")));
        Servico depois = antes.withSolicitantes(new ArrayList<>(asList("primeiro", "segundo", "")));

        ModelAndView mav = controller.adicionarSolicitante(antes);

        assertModelAttributeValue(mav, "servico", depois);
    }

    @Test
    public void deveAdicionarLegislacao() throws Exception {
        Servico antes = new Servico().withLegislacoes(new ArrayList<>(asList("primeiro", "segundo")));
        Servico depois = antes.withLegislacoes(new ArrayList<>(asList("primeiro", "segundo", "")));

        ModelAndView mav = controller.adicionarLegislacao(antes);

        assertModelAttributeValue(mav, "servico", depois);
    }

}