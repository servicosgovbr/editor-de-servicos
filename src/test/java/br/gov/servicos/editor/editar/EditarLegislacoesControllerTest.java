package br.gov.servicos.editor.editar;

import br.gov.servicos.editor.servicos.Servico;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;

public class EditarLegislacoesControllerTest {

    EditarLegislacoesController controller;

    @Before
    public void setUp() throws Exception {
        controller = new EditarLegislacoesController();
    }

    @Test
    public void deveAdicionarLegislacao() throws Exception {
        Servico antes = new Servico().withLegislacoes(new ArrayList<>(asList("primeiro", "segundo")));
        Servico depois = antes.withLegislacoes(new ArrayList<>(asList("primeiro", "segundo", "")));

        ModelAndView mav = controller.adicionarLegislacao(antes);

        assertModelAttributeValue(mav, "servico", depois);
    }

    @Test
    public void deveRemoverLegislacao() throws Exception {
        Servico antes = new Servico().withLegislacoes(new ArrayList<>(asList("primeiro", "segundo")));
        Servico depois = antes.withLegislacoes(new ArrayList<>(asList("primeiro")));

        ModelAndView mav = controller.removerLegislacao(antes, 1);

        assertModelAttributeValue(mav, "servico", depois);
    }
}