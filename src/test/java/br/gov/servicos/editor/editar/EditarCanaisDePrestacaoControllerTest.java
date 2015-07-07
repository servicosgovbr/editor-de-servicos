package br.gov.servicos.editor.editar;

import br.gov.servicos.editor.servicos.CanalDePrestacao;
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
public class EditarCanaisDePrestacaoControllerTest {

    @Mock
    SalvarController salvarController;

    EditarCanaisDePrestacaoController controller;

    @Before
    public void setUp() throws Exception {
        controller = new EditarCanaisDePrestacaoController(salvarController);
    }

    @Test
    public void deveAdicionarCanalAUmaEtapa() throws Exception {
        Servico antes = new Servico().withEtapas(
                asList(new Etapa().withCanaisDePrestacao(
                        new ArrayList<>(asList(new CanalDePrestacao())))));

        Servico depois = new Servico().withEtapas(
                asList(new Etapa().withCanaisDePrestacao(
                        new ArrayList<>(asList(new CanalDePrestacao(), new CanalDePrestacao())))));

        controller.adicionarCanalDePrestacao(antes, 0, USER);

        verify(salvarController).salvar(depois, USER);
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


        controller.removerCanalDePrestacao(antes, "0,0", USER);

        verify(salvarController).salvar(depois, USER);
    }
}