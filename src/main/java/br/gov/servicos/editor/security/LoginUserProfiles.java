package br.gov.servicos.editor.security;

import br.gov.servicos.editor.usuarios.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Profile("!teste")
public class LoginUserProfiles implements UserProfiles {

    private GerenciadorPermissoes gerenciadorPermissoes;

    @Autowired
    public LoginUserProfiles(GerenciadorPermissoes gerenciadorPermissoes, HttpServletRequest httpServletRequest) {
        this.gerenciadorPermissoes = gerenciadorPermissoes;
    }

    @Override
    public UserProfile get() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            Usuario usuario = (Usuario) principal;
            return new UserProfile()
                    .withId(usuario.getEmailPrimario())
                    .withEmail(usuario.getEmailPrimario())
                    .withName(usuario.getNome())
                    .withPermissao(gerenciadorPermissoes.permissao(usuario.getCpf()));
        } else {
            return new UserProfile();
        }

    }
}
