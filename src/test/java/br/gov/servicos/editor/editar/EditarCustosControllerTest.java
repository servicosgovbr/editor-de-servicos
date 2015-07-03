package br.gov.servicos.editor.editar;

import br.gov.servicos.editor.servicos.Custo;
import br.gov.servicos.editor.servicos.Etapa;
import br.gov.servicos.editor.servicos.Servico;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;

public class EditarCustosControllerTest {

    EditarCustosController controller;

    @Before
    public void setUp() throws Exception {
        controller = new EditarCustosController();
    }

    @Test
    public void deveAdicionarCustoAUmaEtapa() throws Exception {
        Servico antes = new Servico().withEtapas(
                asList(new Etapa().withCustos(
                        new ArrayList<>(asList(
                                new Custo().withDescricao("primeiro"),
                                new Custo().withDescricao("segundo"))))));

        Servico depois = new Servico().withEtapas(
                asList(new Etapa().withCustos(
                        new ArrayList<>(asList(
                                new Custo().withDescricao("primeiro"),
                                new Custo().withDescricao("segundo"),
                                new Custo())))));

        ModelAndView mav = controller.adicionarCusto(antes, 0);

        assertModelAttributeValue(mav, "servico", depois);
    }

    @Test
    public void deveRemoverCustoDeUmaEtapa() throws Exception {
        Servico antes = new Servico().withEtapas(
                asList(new Etapa().withCustos(
                        new ArrayList<>(asList(
                                new Custo().withDescricao("primeiro"),
                                new Custo().withDescricao("segundo"),
                                new Custo().withDescricao("terceiro"))))));

        Servico depois = new Servico().withEtapas(
                asList(new Etapa().withCustos(
                        new ArrayList<>(asList(
                                new Custo().withDescricao("primeiro"),
                                new Custo().withDescricao("terceiro"))))));


        ModelAndView mav = controller.removerCusto(antes, "0,1");

        assertModelAttributeValue(mav, "servico", depois);
    }
}