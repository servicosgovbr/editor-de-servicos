package br.gov.servicos.editor.security.cidadao;


import br.gov.servicos.editor.usuarios.Usuario;
import org.springframework.security.authentication.AuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

public class CidadaoAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, Usuario> {

    @Override
    public Usuario buildDetails(HttpServletRequest request) {
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String cpf = request.getParameter("cpf");

        return new Usuario().withCpf(cpf).withEmailPrimario(email).withNome(nome);
    }
}


