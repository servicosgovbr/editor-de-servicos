package br.gov.servicos.editor.editar;

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
public class EditarLegislacoesControllerTest {

    EditarLegislacoesController controller;

    @Mock
    SalvarController salvarController;

    @Before
    public void setUp() throws Exception {
        controller = new EditarLegislacoesController(salvarController);
        when(salvarController.salvar(anyObject(), anyObject())).thenReturn(new RedirectView("/"));
    }

    @Test
    public void deveAdicionarLegislacao() throws Exception {
        Servico antes = new Servico().withLegislacoes(new ArrayList<>(asList("primeiro", "segundo")));
        Servico depois = antes.withLegislacoes(new ArrayList<>(asList("primeiro", "segundo", "")));

        assertThat(controller.adicionarLegislacao(antes, USER).getUrl(), is("/#legislacoes"));

        verify(salvarController).salvar(depois, USER);
    }

    @Test
    public void deveRemoverLegislacao() throws Exception {
        Servico antes = new Servico().withLegislacoes(new ArrayList<>(asList("primeiro", "segundo")));
        Servico depois = antes.withLegislacoes(new ArrayList<>(asList("primeiro")));

        assertThat(controller.removerLegislacao(antes, 1, USER).getUrl(), is("/#legislacoes"));

        verify(salvarController).salvar(depois, USER);
    }
}