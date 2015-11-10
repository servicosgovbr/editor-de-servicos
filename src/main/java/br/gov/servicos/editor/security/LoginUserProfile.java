package br.gov.servicos.editor.security;

import org.springframework.stereotype.Component;

@Component
public class LoginUserProfile implements UserProfiles {
    @Override
    public UserProfile get() {
        return new UserProfile();
    }
}
