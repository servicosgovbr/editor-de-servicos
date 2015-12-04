package br.gov.servicos.editor.security;

public interface UserProfiles {
    UserProfile get();
    boolean temPermissaoParaOrgao(TipoPermissao publicar, String orgaoId);
    boolean temPermissaoParaOrgaoEPapel(TipoPermissao cadastrar, String siorg, String admin);
}
