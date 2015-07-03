package br.gov.servicos.editor.editar;

import br.gov.servicos.editor.servicos.CanalDePrestacao;
import br.gov.servicos.editor.servicos.Etapa;
import br.gov.servicos.editor.servicos.Servico;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;

public class EditarCanaisDePrestacaoControllerTest {

    private EditarCanaisDePrestacaoController controller;

    @Before
    public void setUp() throws Exception {
        controller = new EditarCanaisDePrestacaoController();
    }

    @Test
    public void deveAdicionarCanalAUmaEtapa() throws Exception {
        Servico antes = new Servico().withEtapas(
                asList(new Etapa().withCanaisDePrestacao(
                        new ArrayList<>(asList(new CanalDePrestacao())))));

        Servico depois = new Servico().withEtapas(
                asList(new Etapa().withCanaisDePrestacao(
                        new ArrayList<>(asList(new CanalDePrestacao(), new CanalDePrestacao())))));

        ModelAndView mav = controller.adicionarCanalDePrestacao(antes, 0);

        assertModelAttributeValue(mav, "servico", depois);
    }

    @Test
    public void deveRemoverCanalDePrestacao() throws Exception {
        Servico antes = new Servico().withEtapas(
                asList(new Etapa().withCanaisDePrestacao(
                        new ArrayList<>(
                                asList(new CanalDePrestacao().withDescricao("0"),
                                        new CanalDePrestacao().withDescricao("1"))))));

        Servico depois = new Servico().withEtapas(
                asList(new Etapa().withCanaisDePrestacao(
                        new ArrayList<>(asList(new CanalDePrestacao().withDescricao("1"))))));


        ModelAndView mav = controller.removerCanalDePrestacao(antes, "0,0");

        assertModelAttributeValue(mav, "servico", depois);
    }
}