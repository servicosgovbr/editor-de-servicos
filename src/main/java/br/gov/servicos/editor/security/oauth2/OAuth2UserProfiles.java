package br.gov.servicos.editor.security.oauth2;

import br.gov.servicos.editor.security.GerenciadorPermissoes;
import br.gov.servicos.editor.security.UserProfile;
import br.gov.servicos.editor.security.UserProfiles;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Profile("!teste")
public class OAuth2UserProfiles implements UserProfiles {

    @Value("${google.user.info.uri}")
    String urlTemplate;

    OAuth2RestOperations restTemplate;
    private GerenciadorPermissoes gerenciadorPermissoes;
    Cache cache;

    @Autowired
    public OAuth2UserProfiles(
            @Value("${google.user.info.uri}") String urlTemplate,
            OAuth2RestOperations restTemplate,
            GerenciadorPermissoes gerenciadorPermissoes,
            CacheManager cacheManager
    ) {
        this.urlTemplate = urlTemplate;
        this.restTemplate = restTemplate;
        this.gerenciadorPermissoes = gerenciadorPermissoes;
        this.cache = cacheManager.getCache("google-profiles");
    }

    @Override
    public UserProfile get() {
        return Optional.ofNullable(recuperaUsuario())
                .orElseGet(() -> buscaUsuario());
    }

    private UserProfile recuperaUsuario() {
        return cache.get(token(), UserProfile.class);
    }

    private UserProfile buscaUsuario() {
        UserProfile profile = restTemplate.getForEntity(urlTemplate, UserProfile.class, token()).getBody();
        cache.put(token(), profile
          .withPermissao(gerenciadorPermissoes.permissao(profile.getEmail())));
        return profile;
    }

    private String token() {
        return restTemplate.getAccessToken().toString();
    }

}
