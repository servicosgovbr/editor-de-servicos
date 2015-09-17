package br.gov.servicos.editor.oauth2;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserProfiles {

    @Value("${google.user.info.uri}")
    String urlTemplate;

    OAuth2RestOperations restTemplate;
    Cache cache;

    @Autowired
    public UserProfiles(
            @Value("${google.user.info.uri}") String urlTemplate,
            OAuth2RestOperations restTemplate,
            CacheManager cacheManager
    ) {
        this.urlTemplate = urlTemplate;
        this.restTemplate = restTemplate;
        this.cache = cacheManager.getCache("google-profiles");
    }

    public UserProfile get() {
        String token = restTemplate.getAccessToken().toString();

        UserProfile profile = cache.get(token, UserProfile.class);
        if (profile != null) {
            return profile;
        }

        profile = restTemplate.getForEntity(urlTemplate, UserProfile.class, token).getBody();
        cache.put(token, profile);

        return profile;
    }
}
