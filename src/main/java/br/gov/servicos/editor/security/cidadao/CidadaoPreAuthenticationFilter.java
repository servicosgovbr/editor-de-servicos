package br.gov.servicos.editor.security.cidadao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;



public class CidadaoPreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {


   public CidadaoPreAuthenticationFilter(AuthenticationManager authenticationManager) {
       setAuthenticationManager(authenticationManager);
       setAuthenticationDetailsSource(new CidadaoAuthenticationDetailsSource());
   }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return "Cidadao";
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A";
    }
}
