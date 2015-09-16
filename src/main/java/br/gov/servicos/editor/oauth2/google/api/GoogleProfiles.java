package br.gov.servicos.editor.oauth2.google.api;

import br.gov.servicos.editor.oauth2.google.security.GoogleProfile;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GoogleProfiles {

    OAuth2RestOperations restTemplate;

    @Autowired
    public GoogleProfiles(OAuth2RestOperations restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("google-profiles")
    public GoogleProfile get() {
        String url = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + restTemplate.getAccessToken();
        return restTemplate.getForEntity(url, GoogleProfile.class).getBody();
    }

}
