package br.gov.servicos.editor.editar;

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
public class EditarSolicitantesControllerTest {

    EditarSolicitantesController controller;

    @Mock
    SalvarController salvarController;

    @Before
    public void setUp() throws Exception {
        controller = new EditarSolicitantesController(salvarController);
    }

    @Test
    public void deveAdicionarSolicitante() throws Exception {
        Servico antes = new Servico().withSolicitantes(new ArrayList<>(asList("primeiro", "segundo")));
        Servico depois = antes.withSolicitantes(new ArrayList<>(asList("primeiro", "segundo", "")));

        controller.adicionarSolicitante(antes, USER);

        verify(salvarController).salvar(depois, USER);
    }

    @Test
    public void deveRemoverSolicitante() throws Exception {
        Servico antes = new Servico().withSolicitantes(new ArrayList<>(asList("primeiro", "segundo")));
        Servico depois = antes.withSolicitantes(new ArrayList<>(asList("primeiro")));

        controller.removerSolicitante(antes, 1, USER);

        verify(salvarController).salvar(depois, USER);
    }
}