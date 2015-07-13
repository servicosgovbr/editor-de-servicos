package br.gov.servicos.editor.editar;

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
public class EditarEtapasControllerTest {

    EditarEtapasController controller;

    @Mock
    SalvarController salvarController;

    @Before
    public void setUp() throws Exception {
        controller = new EditarEtapasController(salvarController);
        when(salvarController.salvar(anyObject(), anyObject())).thenReturn(new RedirectView("/"));
    }

    @Test
    public void deveAdicionarEtapas() throws Exception {
        Servico antes = new Servico().withEtapas(new ArrayList<>(asList(new Etapa().withTitulo("Etapa 1"))));
        Servico depois = new Servico().withEtapas(asList(new Etapa().withTitulo("Etapa 1"), new Etapa()));

        assertThat(controller.adicionarEtapa(antes, USER).getUrl(), is("/#etapas[1]"));

        verify(salvarController).salvar(depois, USER);
    }

    @Test
    public void deveRemoverEtapa() throws Exception {
        Servico antes = new Servico().withEtapas(new ArrayList<>(asList(new Etapa(), new Etapa().withTitulo("blah"))));
        Servico depois = new Servico().withEtapas(new ArrayList<>(asList(new Etapa())));

        assertThat(controller.removerEtapa(antes, 1, USER).getUrl(), is("/#etapas[0]"));

        verify(salvarController).salvar(depois, USER);
    }

}
