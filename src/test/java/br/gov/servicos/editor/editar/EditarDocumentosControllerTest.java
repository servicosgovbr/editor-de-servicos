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
public class EditarDocumentosControllerTest {

    EditarDocumentosController controller;

    @Mock
    SalvarController salvarController;

    @Before
    public void setUp() throws Exception {
        controller = new EditarDocumentosController(salvarController);
        when(salvarController.salvar(anyObject(), anyObject())).thenReturn(new RedirectView("/"));
    }

    @Test
    public void deveAdicionarDocumentoAUmaEtapa() throws Exception {
        Servico antes = new Servico().withEtapas(
                asList(new Etapa().withDocumentos(
                        new ArrayList<>(asList("primeiro", "segundo")))));

        Servico depois = new Servico().withEtapas(
                asList(new Etapa().withDocumentos(
                        new ArrayList<>(asList("primeiro", "segundo", "")))));

        assertThat(controller.adicionarDocumento(antes, 0, USER).getUrl(), is("/#etapas[0].documentos"));

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


        assertThat(controller.removerDocumento(antes, "0,1", USER).getUrl(), is("/#etapas[0].documentos"));

        verify(salvarController).salvar(depois, USER);
    }
}