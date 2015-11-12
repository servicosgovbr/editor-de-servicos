package br.gov.servicos.editor.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Profile("!teste")
public class LoginUserProfile implements UserProfiles {

    private GerenciadorPermissoes gerenciadorPermissoes;

    @Autowired
    public LoginUserProfile(GerenciadorPermissoes gerenciadorPermissoes, HttpServletRequest httpServletRequest) {
        this.gerenciadorPermissoes = gerenciadorPermissoes;
    }

    @Override
    public UserProfile get() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return new UserProfile()
                    .withId(username)
                    .withEmail(username)
                    .withName(username)
                    .withPermissao(gerenciadorPermissoes.permissao(username));
        } else {
            return new UserProfile();
        }

    }
}
