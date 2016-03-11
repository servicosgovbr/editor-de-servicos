package br.gov.servicos.editor.security;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class CustomAccessDeniedHandlerTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private CustomAccessDeniedHandler handler;

    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        handler = new CustomAccessDeniedHandler();
    }

    @Test
    public void redirecionaParaAutenticacaoQuandoTokenCsrfÉInvalido() throws Exception {
        AccessDeniedException exception = new InvalidCsrfTokenException(
                new DefaultCsrfToken("header", "param", "token"),
                "actualToken"
        );

        handler.handle(request, response, exception);

        assertThat(response.getRedirectedUrl(), is("/editar/autenticar?sessao"));
    }

    @Test
    public void redirecionaParaAutenticacaoQuandoTokenCsrfEstáAusente() throws Exception {
        AccessDeniedException exception = new MissingCsrfTokenException(
                "actualToken"
        );

        handler.handle(request, response, exception);

        assertThat(response.getRedirectedUrl(), is("/editar/autenticar?sessao"));
    }

    @Test
    public void ignoraOutrosTiposDeExceção() throws Exception {
        AccessDeniedException exception = new AccessDeniedException("algum outro erro");

        handler.handle(request, response, exception);

        assertThat(response.getRedirectedUrl(), is(nullValue()));
    }
}