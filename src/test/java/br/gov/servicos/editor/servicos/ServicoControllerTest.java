package br.gov.servicos.editor.servicos;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

import static br.gov.servicos.fixtures.TestData.SERVICO;
import static org.springframework.test.web.ModelAndViewAssert.assertModelAttributeValue;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;

public class ServicoControllerTest {

    private ServicoController controller;

    @Before
    public void setUp() throws Exception {
        File cartasServico = new ClassPathResource("repositorio-cartas-servico").getFile();
        controller = new ServicoController(cartasServico);
    }

    @Test
    public void aoEditarServicoDeveRedireciarParaOIndex() {
        assertViewName(controller.editar("exemplo-servico"), "index");
    }

    @Test
    public void aoEditarServicoDeveCarregarOServico() {
        assertModelAttributeValue(controller.editar("exemplo-servico"), "servico", SERVICO);
    }

}