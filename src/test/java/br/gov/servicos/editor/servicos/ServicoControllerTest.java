package br.gov.servicos.editor.servicos;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;

import static br.gov.servicos.fixtures.TestData.SERVICO;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
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
        ModelAndView resultado = controller.editar("exemplo-servico");
        assertThat(resultado.getModel().get("servico"), is(SERVICO));
    }

}