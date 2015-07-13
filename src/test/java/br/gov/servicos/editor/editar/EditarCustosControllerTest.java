package br.gov.servicos.editor.editar;

import br.gov.servicos.editor.servicos.Custo;
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
public class EditarCustosControllerTest {

    EditarCustosController controller;

    @Mock
    SalvarController salvarController;


    @Before
    public void setUp() throws Exception {
        controller = new EditarCustosController(salvarController);
        when(salvarController.salvar(anyObject(), anyObject())).thenReturn(new RedirectView("/"));
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

        assertThat(controller.adicionarCusto(antes, 0, USER).getUrl(), is("/#etapas[0].custos"));

        verify(salvarController).salvar(depois, USER);
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


        assertThat(controller.removerCusto(antes, "0,1", USER).getUrl(), is("/#etapas[0].custos"));

        verify(salvarController).salvar(depois, USER);
    }
}