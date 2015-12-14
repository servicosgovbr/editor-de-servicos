package br.gov.servicos.editor.security;


import br.gov.servicos.editor.conteudo.TipoPagina;



public abstract class CheckOrgaoEspecificoController {


   public boolean usuarioPodeRealizarAcao(UserProfiles userProfiles, TipoPagina tipoPagina, String orgaoId) {
        TipoPermissao tipoPermissao = getTipoPermissao();


       if (TipoPagina.PAGINA_TEMATICA.equals(tipoPagina)) {
          return usuarioTemPermissaoParaTipoPagina(tipoPermissao,userProfiles,tipoPagina);
       }

       return usuarioTemPermissaoParaTipoPagina(tipoPermissao,userProfiles,tipoPagina) ||
                userProfiles.temPermissaoParaTipoPaginaOrgaoEspecifico(tipoPermissao,tipoPagina, orgaoId);

    }

   public abstract TipoPermissao getTipoPermissao();


    private final boolean usuarioTemPermissaoParaTipoPagina(TipoPermissao tipoPermissao, UserProfiles userProfiles, TipoPagina tipoPagina) {
        return userProfiles.temPermissaoParaTipoPagina(tipoPermissao, tipoPagina);
    }
}
