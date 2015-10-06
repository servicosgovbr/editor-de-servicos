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

import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static java.lang.String.format;
import static java.nio.charset.Charset.defaultCharset;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.security.crypto.codec.Base64.encode;

/**
 * Copied from Spring Security OAuth2 to support the custom format for a Google Token which is different from what Spring supports
 */
@Slf4j
@Component
@FieldDefaults(level = PRIVATE)
public class GoogleTokenServices extends RemoteTokenServices {

    @Value("${google.client.id}")
    String clientId;

    @Value("${google.client.secret}")
    String clientSecret;

    @Value("${google.token.info.uri}")
    String tokenInfoUri;

    RestOperations restTemplate;
    AccessTokenConverter tokenConverter;

    @Autowired
    public GoogleTokenServices(GoogleAccessTokenConverter accessTokenConverter, RestTemplate restTemplate) {
        this.tokenConverter = accessTokenConverter;
        this.restTemplate = restTemplate;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        Map<String, Object> checkTokenResponse = checkToken(accessToken);

        if (checkTokenResponse.containsKey("error")) {
            logger.debug("check_token returned error: " + checkTokenResponse.get("error"));
            throw new InvalidTokenException(accessToken);
        }

        transformNonStandardValuesToStandardValues(checkTokenResponse);

        Assert.state(checkTokenResponse.containsKey("client_id"), "Client id must be present in response from auth server");
        return tokenConverter.extractAuthentication(checkTokenResponse);
    }

    private Map<String, Object> checkToken(String accessToken) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("token", accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader(clientId, clientSecret));
        headers.setContentType(APPLICATION_FORM_URLENCODED);

        ParameterizedTypeReference<Map<String, Object>> map = new ParameterizedTypeReference<Map<String, Object>>() {
        };

        return restTemplate.exchange(tokenInfoUri, POST, new HttpEntity<>(formData, headers), map, accessToken).getBody();
    }

    private void transformNonStandardValuesToStandardValues(Map<String, Object> map) {
        map.put("client_id", map.get("issued_to")); // Google sends 'client_id' as 'issued_to'
        map.put("user_name", map.get("email"));
    }

    @SneakyThrows
    private String authorizationHeader(String clientId, String clientSecret) {
        return "Basic " + new String(encode(format("%s:%s", clientId, clientSecret).getBytes(defaultCharset())), defaultCharset());
    }

}