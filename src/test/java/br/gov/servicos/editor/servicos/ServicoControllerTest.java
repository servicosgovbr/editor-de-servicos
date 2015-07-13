package br.gov.servicos.editor.servicos;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Optional;

import static br.gov.servicos.fixtures.TestData.SERVICO_V2;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;

@RunWith(MockitoJUnitRunner.class)
public class ServicoControllerTest {

    ServicoController controller;

    @Mock
    ImportadorServicoV2 v2;

    @Before
    public void setUp() throws Exception {
        controller = new ServicoController(v2);
    }

    @Test
    public void aoEditarServicoDeveRedireciarParaOIndex() throws IOException {
        given(v2.carregar("exemplo-servico")).willReturn(Optional.of(SERVICO_V2));

        assertViewName(controller.editar("exemplo-servico"), "index");
    }

    @Test
    public void aoEditarServicoDeveCarregarOServico() throws IOException {
        given(v2.carregar("exemplo-servico")).willReturn(Optional.of(SERVICO_V2));

        ModelAndView resultado = controller.editar("exemplo-servico");

        assertThat(resultado.getModel().get("servico"), is(SERVICO_V2));
    }
}