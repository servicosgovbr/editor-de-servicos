package br.gov.servicos.editor.security;

public interface UserProfiles {
    UserProfile get();
    boolean temPermissaoParaOrgao(TipoPermissao publicar, String orgaoId);
}
