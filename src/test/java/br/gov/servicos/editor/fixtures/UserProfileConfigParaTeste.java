package br.gov.servicos.editor.fixtures;

import br.gov.servicos.editor.conteudo.TipoPagina;
import br.gov.servicos.editor.security.TipoPermissao;
import br.gov.servicos.editor.security.UserProfile;
import br.gov.servicos.editor.security.UserProfiles;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE)
@Profile("teste")
public class UserProfileConfigParaTeste implements UserProfiles {

    @Setter
    private boolean temPermissaoParaOrgao = true;

    @Override
    public UserProfile get() {
        return new UserProfile()
                .withEmail("teste@gmail.com")
                .withName("teste")
                .withFamilyName("teste");
    }

    @Override
    public boolean temPermissaoParaOrgao(TipoPermissao publicar, String orgaoId) {
        return temPermissaoParaOrgao;
    }

    @Override
    public boolean temPermissaoParaTipoPagina(TipoPermissao tipoPermissao, TipoPagina tipoPagina) {
        return true;
    }

    @Override
    public boolean temPermissaoGerenciarUsuarioOrgaoEPapel(String siorg, String admin) {
        return true;
    }

    @Override
    public boolean temPermissao(String permissao) {
        return true;
    }



    @Override
    public String getSiorg() {
        return "";
    }

}