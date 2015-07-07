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
public class EditarDocumentosControllerTest {

    EditarDocumentosController controller;

    @Mock
    SalvarController salvarController;

    @Before
    public void setUp() throws Exception {
        controller = new EditarDocumentosController(salvarController);
    }

    @Test
    public void deveAdicionarDocumentoAUmaEtapa() throws Exception {
        Servico antes = new Servico().withEtapas(
                asList(new Etapa().withDocumentos(
                        new ArrayList<>(asList("primeiro", "segundo")))));

        Servico depois = new Servico().withEtapas(
                asList(new Etapa().withDocumentos(
                        new ArrayList<>(asList("primeiro", "segundo", "")))));

        controller.adicionarDocumento(antes, 0, USER);

        verify(salvarController).salvar(depois, USER);
    }

    @Test
    public void deveRemoverDocumentoDeUmaEtapa() throws Exception {
        Servico antes = new Servico().withEtapas(
                asList(new Etapa().withDocumentos(
                        new ArrayList<>(asList("primeiro", "segundo", "terceiro")))));

        Servico depois = new Servico().withEtapas(
                asList(new Etapa().withDocumentos(
                        new ArrayList<>(asList("primeiro", "terceiro")))));


        controller.removerDocumento(antes, "0,1", USER);

        verify(salvarController).salvar(depois, USER);
    }
}