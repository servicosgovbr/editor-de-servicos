package br.gov.servicos.editor.security;

import br.gov.servicos.editor.conteudo.TipoPagina;

import static br.gov.servicos.editor.conteudo.TipoPagina.PAGINA_TEMATICA;

public abstract class CheckOrgaoEspecificoController {

    public boolean usuarioPodeRealizarAcao(UserProfiles perfil, TipoPagina tipoPagina, String orgaoId) {
        TipoPermissao permissao = getTipoPermissao();

        if (PAGINA_TEMATICA.equals(tipoPagina)) {
            return usuarioTemPermissaoParaTipoPagina(permissao, perfil, tipoPagina);
        }

        return usuarioTemPermissaoParaTipoPagina(permissao, perfil, tipoPagina) ||
                perfil.temPermissaoParaTipoPaginaOrgaoEspecifico(permissao, tipoPagina, orgaoId);
    }

    public abstract TipoPermissao getTipoPermissao();

    private boolean usuarioTemPermissaoParaTipoPagina(TipoPermissao tipoPermissao, UserProfiles userProfiles, TipoPagina tipoPagina) {
        return userProfiles.temPermissaoParaTipoPagina(tipoPermissao, tipoPagina);
    }

}
