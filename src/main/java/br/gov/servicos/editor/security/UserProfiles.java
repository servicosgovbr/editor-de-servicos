package br.gov.servicos.editor.security;

import br.gov.servicos.editor.conteudo.TipoPagina;

public interface UserProfiles {
    UserProfile get();
    boolean temPermissaoParaOrgao(TipoPagina tipoPagina, String orgaoId);
}
