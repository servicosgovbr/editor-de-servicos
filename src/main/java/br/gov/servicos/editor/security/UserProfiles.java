package br.gov.servicos.editor.security;

import br.gov.servicos.editor.conteudo.TipoPagina;

public interface UserProfiles {
    UserProfile get();
    boolean temPermissaoParaOrgao(TipoPermissao publicar, String orgaoId);
    boolean temPermissaoParaTipoPagina(TipoPermissao tipoPermissao, TipoPagina tipoPagina);
    boolean temPermissaoGerenciarUsuarioOrgaoEPapel(String siorg, String admin);
    boolean temPermissao(String permissao);
    String getSiorg();
}
