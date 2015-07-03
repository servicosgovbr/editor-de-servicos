package br.gov.servicos.editor.editar;

import br.gov.servicos.editor.servicos.Etapa;
import br.gov.servicos.editor.servicos.Servico;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;

public class EditarDocumentosControllerTest {

    EditarDocumentosController controller;

    @Before
    public void setUp() throws Exception {
        controller = new EditarDocumentosController();
    }

    @Test
    public void deveAdicionarDocumentoAUmaEtapa() throws Exception {
        Servico antes = new Servico().withEtapas(
                asList(new Etapa().withDocumentos(
                        new ArrayList<>(asList("primeiro", "segundo")))));

        Servico depois = new Servico().withEtapas(
                asList(new Etapa().withDocumentos(
                        new ArrayList<>(asList("primeiro", "segundo", "")))));

        ModelAndView mav = controller.adicionarDocumento(antes, 0);

        assertModelAttributeValue(mav, "servico", depois);
    }

    @Test
    public void deveRemoverDocumentoDeUmaEtapa() throws Exception {
        Servico antes = new Servico().withEtapas(
                asList(new Etapa().withDocumentos(
                        new ArrayList<>(asList("primeiro", "segundo", "terceiro")))));

        Servico depois = new Servico().withEtapas(
                asList(new Etapa().withDocumentos(
                        new ArrayList<>(asList("primeiro", "terceiro")))));


        ModelAndView mav = controller.removerDocumento(antes, "0,1");

        assertModelAttributeValue(mav, "servico", depois);
    }
}