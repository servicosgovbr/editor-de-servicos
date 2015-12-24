package br.gov.servicos.editor.security;

import lombok.experimental.FieldDefaults;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.RedirectView;

import static lombok.AccessLevel.PRIVATE;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;

@FieldDefaults(level = PRIVATE)
@RunWith(MockitoJUnitRunner.class)
public class AcessoCidadaoControllerTest {

    AcessoCidadaoController controller;

    @Mock
    AcessoCidadaoService service;

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
        doNothing().when(service).autenticaCidadao(anyString(), anyString(), anyString());
        RedirectView view = controller.acessoCidadao("Nome", "um@email.com", "123.123.123.12");
        assertThat(view.getUrl(), is("/editar"));
    }
}
