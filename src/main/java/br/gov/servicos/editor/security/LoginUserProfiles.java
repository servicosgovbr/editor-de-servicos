package br.gov.servicos.editor.security;

import br.gov.servicos.editor.config.SlugifyConfig;
import br.gov.servicos.editor.conteudo.TipoPagina;
import br.gov.servicos.editor.usuarios.Usuario;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import static br.gov.servicos.editor.security.TipoPermissao.CADASTRAR_OUTROS_ORGAOS;

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
                    .withName(usuario.getNome())
                    .withPermissoes(usuario.getAuthorities())
                    .withSiorg(usuario.getSiorg());
        } else {
            return new UserProfile();
        }
    }

    public boolean temPermissaoParaOrgao(TipoPermissao permissao, String orgaoId) {
        Usuario usuario = getPrincipal();
        return usuario.temPermissaoComOrgao(permissao, orgaoId);
    }

    @Override
    public boolean temPermissaoParaTipoPagina(TipoPermissao tipoPermissao, TipoPagina tipoPagina) {
        Usuario usuario = getPrincipal();
        return usuario.temPermissao(tipoPermissao.comTipoPagina(tipoPagina));
    }

    public boolean temPermissaoParaCriarAdmin() {
        Usuario usuario = getPrincipal();
        return usuario.temPermissao(TipoPermissao.CADASTRAR.comPapel("ADMIN"));
    }

    @Override
    public boolean temPermissaoGerenciarUsuarioOrgaoEPapel(String siorg, String papel) {
        Usuario usuario = getPrincipal();
        return usuario.temPermissao(TipoPermissao.CADASTRAR.comPapel(papel.toUpperCase())) &&
                (usuario.getSiorg().equals(siorg) || usuario.temPermissao(CADASTRAR_OUTROS_ORGAOS.getNome()));
    }

    @Override
    public boolean temPermissaoParaTipoPaginaOrgaoEspecifico(TipoPermissao tipoPermissao, TipoPagina tipoPagina, String unsafeOrgaoId) {
        Usuario usuario = getPrincipal();
        String userSiorgId = SlugifyConfig.slugify(usuario.getSiorg());
        String orgaoId = SlugifyConfig.slugify(unsafeOrgaoId);

        return temPermissao(tipoPermissao.comTipoPaginaParaOrgaoEspecifico(tipoPagina)) && userSiorgId.equals(orgaoId);
    }

    @Override
    public boolean temPermissao(String permissao) {
        return getPrincipal().temPermissao(permissao);
    }

    @Override
    public String getSiorg() {
        return getPrincipal().getSiorg();
    }

    private Usuario getPrincipal() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
