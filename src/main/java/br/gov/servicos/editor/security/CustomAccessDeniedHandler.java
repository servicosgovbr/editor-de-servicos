package br.gov.servicos.editor.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler extends AccessDeniedHandlerImpl {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {

        request.getSession();

        if (accessDeniedException instanceof InvalidCsrfTokenException ||
                accessDeniedException instanceof MissingCsrfTokenException) {
            new DefaultRedirectStrategy().sendRedirect(request, response, "/editar/autenticar?sessao");
        }
        super.handle(request, response, accessDeniedException);
    }
}
