package br.gov.servicos.editor.security;

import br.gov.servicos.editor.usuarios.FormularioAcessoCidadao;
import lombok.experimental.FieldDefaults;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import static lombok.AccessLevel.PRIVATE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@FieldDefaults(level = PRIVATE)
@RunWith(MockitoJUnitRunner.class)
public class AcessoCidadaoControllerTest {

    AcessoCidadaoController controller;

    @Mock
    AcessoCidadaoService service;

    @Mock
    BindingResult result;

    @Before
    public void setUp() {
        controller = new AcessoCidadaoController(service);
    }

    @Test
    public void deveRedirecionarParaAcessoCidadao() {
        assertThat(controller.acessoCidadao().getViewName(), is("acesso-cidadao"));
    }

    @Test
    public void deveRedirecionarParaPaginaPrincipal() {
        FormularioAcessoCidadao cidadao = criaCidadao();
        when(result.hasErrors()).thenReturn(false);
        doNothing().when(service).autenticaCidadao(eq(cidadao));


        ModelAndView view = controller.acessoCidadao(cidadao, result);
        assertThat(view.getViewName(), is("redirect:/editar"));
    }

    @Test
    public void naoDeveRedirecionarParaPaginaPrincipalPoisExisteErroDeValidacao() {
        FormularioAcessoCidadao cidadao = criaCidadao();
        when(result.hasErrors()).thenReturn(true);
        doNothing().when(service).autenticaCidadao(eq(cidadao));


        ModelAndView view = controller.acessoCidadao(cidadao, result);
        assertThat(view.getViewName(), is("acesso-cidadao"));
    }

    private FormularioAcessoCidadao criaCidadao() {
        return new FormularioAcessoCidadao("Nome", "um@email.com", "123.123.123-12");
    }
}
