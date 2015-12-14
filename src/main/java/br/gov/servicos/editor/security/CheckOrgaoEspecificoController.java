package br.gov.servicos.editor.security;


import br.gov.servicos.editor.conteudo.TipoPagina;
import br.gov.servicos.editor.frontend.Siorg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public abstract class CheckOrgaoEspecificoController {


   public boolean usuarioPodeRealizarAcao(UserProfiles userProfiles, TipoPagina tipoPagina, String orgaoId) {
        TipoPermissao tipoPermissao = getTipoPermissao();

        return userProfiles.temPermissaoParaTipoPagina(tipoPermissao, tipoPagina) ||
                userProfiles.temPermissaoParaTipoPaginaOrgaoEspecifico(tipoPermissao,tipoPagina, orgaoId);

    }

   public abstract TipoPermissao getTipoPermissao();


}
