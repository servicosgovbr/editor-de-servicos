/*******************************************************************************
 * Cloud Foundry
 * Copyright (c) [2009-2014] Pivotal Software, Inc. All Rights Reserved.
 * <p>
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 * <p>
 * This product includes a number of subcomponents with
 * separate copyright notices and license terms. Your use of these
 * subcomponents is subject to the terms and conditions of the
 * subcomponent's license, as noted in the LICENSE file.
 *******************************************************************************/
package br.gov.servicos.editor.security.oauth2.google;

import br.gov.servicos.editor.security.GerenciadorPermissoes;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GoogleAccessTokenConverter extends DefaultAccessTokenConverter {

    UserAuthenticationConverter userTokenConverter;

    @Autowired
    public GoogleAccessTokenConverter(GerenciadorPermissoes gerenciadorPermissoes) {
        DefaultUserAuthenticationConverter defaultConverter = new DefaultUserAuthenticationConverter();
        defaultConverter.setUserDetailsService(
                username -> new User(
                        username,
                        "N/A",
                        gerenciadorPermissoes.authorities(username)));

        setUserTokenConverter(defaultConverter);
        userTokenConverter = defaultConverter;
    }

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        Map<String, String> parameters = new HashMap<>();
        String clientId = (String) map.get(CLIENT_ID);
        parameters.put(CLIENT_ID, clientId);

        return new OAuth2Authentication(
                new OAuth2Request(parameters, clientId, null, true,
                        scopesFrom(map), resourceIdsFrom(map), null, null, null),
                userTokenConverter.extractAuthentication(map));
    }

    @SuppressWarnings("unchecked")
    private Set<String> resourceIdsFrom(Map<String, ?> map) {
        return new LinkedHashSet<>((Collection<String>) ofNullable(map.get(AUD)).orElse(emptyList()));
    }

    @SuppressWarnings("unchecked")
    private Set<String> scopesFrom(Map<String, ?> map) {
        // Parsing of scopes coming back from Google are slightly different from the default implementation
        // Instead of it being a collection it is a String where multiple scopes are separated by a space.
        return ofNullable(map.get(SCOPE)).map(s -> {
            if (s instanceof Collection) {
                return new LinkedHashSet<>((Collection<String>) s);

            } else if (s instanceof String) {
                return new LinkedHashSet<>(asList(((String) s).split(" ")));
            }

            return Collections.<String>emptySet();
        }).orElse(Collections.<String>emptySet());
    }
}