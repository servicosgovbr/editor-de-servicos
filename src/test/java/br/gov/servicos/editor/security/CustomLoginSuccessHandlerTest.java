package br.gov.servicos.editor.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomLoginSuccessHandlerTest {

    public static final String DEFAULT_REDIRECT_URL = "/editar";
    public static final String LOGIN_URL = "/editar/autenticar";
    @Mock
    RequestCache requestCache;

    @Mock
    RedirectStrategy redirectStrategy;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    CustomLoginSuccessHandler successHandler;


    @Before
    public void setUp () throws ServletException, IOException {
        successHandler  = new CustomLoginSuccessHandler(DEFAULT_REDIRECT_URL, LOGIN_URL);
        successHandler.setRedirectStrategy(redirectStrategy);
    }

    @Test
    public void deveRedirecionarParaUrlPadrãoSeSavedRequestForNull() throws ServletException, IOException {
        when(requestCache.getRequest(any(), any())).thenReturn(null);

        successHandler.onAuthenticationSuccess(request, response, authentication);
        verify(redirectStrategy).sendRedirect(request, response, DEFAULT_REDIRECT_URL);

    }

    @Test
    public void deveRedirecionarParaUrlPadrãoSeUrlAnteriorForAutenticar() throws ServletException, IOException {
        SavedRequest savedRequest = mock(SavedRequest.class);
        when(savedRequest.getRedirectUrl()).thenReturn(LOGIN_URL);
        when(requestCache.getRequest(any(), any())).thenReturn(savedRequest);
        successHandler.setRequestCache(requestCache);

        successHandler.onAuthenticationSuccess(request, response, authentication);
        verify(redirectStrategy).sendRedirect(request, response, DEFAULT_REDIRECT_URL);
    }

    @Test
    public void deveRedirecionarParaUrlAnteriorSeForDiferenteDaUrlDoLogin() throws ServletException, IOException {
        SavedRequest savedRequest = mock(SavedRequest.class);
        String outraUrl = "/editar/outro";
        when(savedRequest.getRedirectUrl()).thenReturn(outraUrl);
        when(requestCache.getRequest(any(), any())).thenReturn(savedRequest);
        successHandler.setRequestCache(requestCache);

        successHandler.onAuthenticationSuccess(request, response, authentication);
        verify(redirectStrategy).sendRedirect(request, response, outraUrl);
    }




}