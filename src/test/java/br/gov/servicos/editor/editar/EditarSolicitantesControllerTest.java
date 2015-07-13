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
public class EditarSolicitantesControllerTest {

    EditarSolicitantesController controller;

    @Mock
    SalvarController salvarController;

    @Before
    public void setUp() throws Exception {
        controller = new EditarSolicitantesController(salvarController);
        when(salvarController.salvar(anyObject(), anyObject())).thenReturn(new RedirectView("/"));
    }

    @Test
    public void deveAdicionarSolicitante() throws Exception {
        Servico antes = new Servico().withSolicitantes(new ArrayList<>(asList("primeiro", "segundo")));
        Servico depois = antes.withSolicitantes(new ArrayList<>(asList("primeiro", "segundo", "")));

        assertThat(controller.adicionarSolicitante(antes, USER).getUrl(), is("/#solicitantes"));

        verify(salvarController).salvar(depois, USER);
    }

    @Test
    public void deveRemoverSolicitante() throws Exception {
        Servico antes = new Servico().withSolicitantes(new ArrayList<>(asList("primeiro", "segundo")));
        Servico depois = antes.withSolicitantes(new ArrayList<>(asList("primeiro")));

        assertThat(controller.removerSolicitante(antes, 1, USER).getUrl(), is("/#solicitantes"));

        verify(salvarController).salvar(depois, USER);
    }
}