package br.gov.servicos.editor.security;

import br.gov.servicos.editor.usuarios.PapelRepository;
import br.gov.servicos.editor.usuarios.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;


@Component
public class CidadaoAuthenticationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    public static final String CIDADAO = "CIDADAO";

    @Autowired
    PapelRepository repository;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        Usuario usuario = (Usuario) token.getDetails();
        return usuario.withPapel(repository.findByNome(CIDADAO));
    }
}
