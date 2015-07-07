package br.gov.servicos.editor.editar;

import br.gov.servicos.editor.servicos.Etapa;
import br.gov.servicos.editor.servicos.Servico;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static br.gov.servicos.fixtures.TestData.USER;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EditarEtapasControllerTest {

    EditarEtapasController controller;

    @Mock
    SalvarController salvarController;

    @Before
    public void setUp() throws Exception {
        controller = new EditarEtapasController(salvarController);
    }

    @Test
    public void deveAdicionarEtapas() throws Exception {
        Servico antes = new Servico().withEtapas(new ArrayList<>(asList(new Etapa().withTitulo("Etapa 1"))));
        Servico depois = new Servico().withEtapas(asList(new Etapa().withTitulo("Etapa 1"), new Etapa()));

        controller.adicionarEtapa(antes, USER);

        verify(salvarController).salvar(depois, USER);
    }

    @Test
    public void deveRemoverEtapa() throws Exception {
        Servico antes = new Servico().withEtapas(new ArrayList<>(asList(new Etapa(), new Etapa().withTitulo("blah"))));
        Servico depois = new Servico().withEtapas(new ArrayList<>(asList(new Etapa())));

        controller.removerEtapa(antes, 1, USER);

        verify(salvarController).salvar(depois, USER);
    }

}
