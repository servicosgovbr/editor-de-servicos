package br.gov.servicos.editor.editar;

import br.gov.servicos.editor.servicos.Custo;
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
public class EditarCustosControllerTest {

    EditarCustosController controller;

    @Mock
    SalvarController salvarController;


    @Before
    public void setUp() throws Exception {
        controller = new EditarCustosController(salvarController);
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

        controller.adicionarCusto(antes, 0, USER);

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


        controller.removerCusto(antes, "0,1", USER);

        verify(salvarController).salvar(depois, USER);
    }
}