package br.gov.servicos.editor.fixtures;

import br.gov.servicos.editor.security.UserProfile;
import br.gov.servicos.editor.security.UserProfiles;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static br.gov.servicos.editor.security.Permissao.ADMIN;
import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Profile("teste")
public class UserProfileConfigParaTeste implements UserProfiles {

    @Override
    public UserProfile get() {
        return new UserProfile()
                .withEmail("teste@gmail.com")
                .withName("teste")
                .withFamilyName("teste")
                .withPermissao(ADMIN);
    }

}