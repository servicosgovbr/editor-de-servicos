package br.gov.servicos.editor.oauth2.google.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import javax.annotation.Resource;

import static java.util.Arrays.asList;
import static org.springframework.security.oauth2.common.AuthenticationScheme.form;
import static org.springframework.security.oauth2.common.AuthenticationScheme.query;

@Order(1)
@Configuration
@EnableOAuth2Client
@EnableWebSecurity
@ImportResource({"classpath:security-context.xml"})
public class SecurityWebAppInitializer extends AbstractSecurityWebApplicationInitializer {

    @Resource
    @Qualifier("accessTokenRequest")
    AccessTokenRequest accessTokenRequest;

    @Value("${google.client.id}")
    String clientId;

    @Value("${google.client.secret}")
    String clientSecret;

    @Value("${google.access.token.uri}")
    String accessTokenUri;

    @Value("${google.user.authorization.uri}")
    String userAuthorizationUri;

    @Value("${google.authorization.code}")
    String authorizationCode;

    @Value("${google.auth.scope}")
    String authScope;

    @Value("${google.preestablished.redirect.url}")
    String preestablishedRedirectUrl;

    /**
     * This authentication entry point is used for all the unauthenticated or unauthorised sessions to be directed to the
     * /googleLogin URL which is then intercepted by the oAuth2AuthenticationProcessingFilter to trigger authentication from
     * Google.
     */
    @Bean
    AuthenticationEntryPoint clientAuthenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint("/googleLogin");
    }

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    OAuth2RestTemplate googleRestTemplate() {
        return new OAuth2RestTemplate(googleResource(), new DefaultOAuth2ClientContext(accessTokenRequest));
    }

    @Bean
    @Scope("session")
    OAuth2ProtectedResourceDetails googleResource() {
        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();

        details.setId("google-oauth-client");
        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        details.setAccessTokenUri(accessTokenUri);
        details.setUserAuthorizationUri(userAuthorizationUri);
        details.setTokenName(authorizationCode);
        details.setScope(asList(authScope.split(",")));

        details.setUseCurrentUri(false);
        details.setAuthenticationScheme(query);
        details.setClientAuthenticationScheme(form);

        return details;
    }

}
