package br.gov.servicos.editor.security;

import br.gov.servicos.editor.usuarios.Usuario;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Profile("!teste")
public class LoginUserProfiles implements UserProfiles {
    @Override
    public UserProfile get() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            Usuario usuario = (Usuario) principal;
            return new UserProfile()
                    .withId(usuario.getEmailPrimario())
                    .withEmail(usuario.getEmailPrimario())
                    .withName(usuario.getNome());
        } else {
            return new UserProfile();
        }
    }

    public boolean temPermissaoParaOrgao(TipoPermissao permissao, String orgaoId) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuario.temPermissaoComOrgao(permissao, orgaoId);
    }

    @Override
    public boolean temPermissaoGerenciarUsuarioOrgaoEPapel(String siorg, String admin) {
        return false;
    }
}
