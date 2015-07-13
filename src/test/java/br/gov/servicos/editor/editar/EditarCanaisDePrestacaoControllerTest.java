package br.gov.servicos.editor.editar;

import br.gov.servicos.editor.servicos.CanalDePrestacao;
import br.gov.servicos.editor.servicos.Etapa;
import br.gov.servicos.editor.servicos.Servico;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;

import static br.gov.servicos.fixtures.TestData.USER;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EditarCanaisDePrestacaoControllerTest {

    @Mock
    SalvarController salvarController;

    EditarCanaisDePrestacaoController controller;

    @Before
    public void setUp() throws Exception {
        controller = new EditarCanaisDePrestacaoController(salvarController);
        when(salvarController.salvar(anyObject(), anyObject())).thenReturn(new RedirectView("/"));
    }

    @Test
    public void deveAdicionarCanalAUmaEtapa() throws Exception {
        Servico antes = new Servico().withEtapas(
                asList(new Etapa().withCanaisDePrestacao(
                        new ArrayList<>(asList(new CanalDePrestacao())))));

        Servico depois = new Servico().withEtapas(
                asList(new Etapa().withCanaisDePrestacao(
                        new ArrayList<>(asList(new CanalDePrestacao(), new CanalDePrestacao())))));

        assertThat(controller.adicionarCanalDePrestacao(antes, 0, USER).getUrl(), is("/#etapas[0].canaisDePrestacao"));

        verify(salvarController).salvar(depois, USER);
    }

    @Test
    public void deveRemoverCanalDePrestacao() throws Exception {
        Servico antes = new Servico().withEtapas(
                asList(new Etapa().withCanaisDePrestacao(
                        new ArrayList<>(
                                asList(new CanalDePrestacao().withCaption("0").withReferencia("ref0"),
                                        new CanalDePrestacao().withCaption("1").withReferencia("ref1"))))));

        Servico depois = new Servico().withEtapas(
                asList(new Etapa().withCanaisDePrestacao(
                        new ArrayList<>(asList(new CanalDePrestacao().withCaption("1").withReferencia("ref1"))))));


        assertThat(controller.removerCanalDePrestacao(antes, "0,0", USER).getUrl(), is("/#etapas[0].canaisDePrestacao"));

        verify(salvarController).salvar(depois, USER);
    }
}