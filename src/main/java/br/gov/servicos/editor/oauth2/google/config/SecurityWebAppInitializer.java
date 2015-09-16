package br.gov.servicos.editor.oauth2.google.config;

import br.gov.servicos.editor.oauth2.google.security.DefaultUserAuthenticationConverter;
import br.gov.servicos.editor.oauth2.google.security.GoogleAccessTokenConverter;
import br.gov.servicos.editor.oauth2.google.security.GoogleTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Order(1)
@Configuration
@EnableOAuth2Client
@EnableWebSecurity
@ImportResource({"classpath:security-context.xml"})
public class SecurityWebAppInitializer extends AbstractSecurityWebApplicationInitializer {

    @Autowired
    private Environment env;

    @Resource
    @Qualifier("accessTokenRequest")
    private AccessTokenRequest accessTokenRequest;

    /**
     * This authentication entry point is used for all the unauthenticated or unauthorised sessions to be directed to the
     * /googleLogin URL which is then intercepted by the oAuth2AuthenticationProcessingFilter to trigger authentication from
     * Google.
     */
    @Bean
    public AuthenticationEntryPoint clientAuthenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint("/googleLogin");
    }

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public OAuth2RestTemplate googleRestTemplate() {
        return new OAuth2RestTemplate(googleResource(), new DefaultOAuth2ClientContext(accessTokenRequest));
    }

    @Bean
    @Scope("session")
    public OAuth2ProtectedResourceDetails googleResource() {
        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
        details.setId("google-oauth-client");
        details.setClientId(env.getProperty("google.client.id"));
        details.setClientSecret(env.getProperty("google.client.secret"));
        details.setAccessTokenUri(env.getProperty("google.accessTokenUri"));
        details.setUserAuthorizationUri(env.getProperty("google.userAuthorizationUri"));
        details.setTokenName(env.getProperty("google.authorization.code"));
        details.setScope(parseScopes(env.getProperty("google.auth.scope")));
        details.setPreEstablishedRedirectUri(env.getProperty("google.preestablished.redirect.url"));
        details.setUseCurrentUri(false);
        details.setAuthenticationScheme(AuthenticationScheme.query);
        details.setClientAuthenticationScheme(AuthenticationScheme.form);
        return details;
    }

    /**
     * These token classes are mostly a clone of the Spring classes but have the structure modified so that the response
     * from Google can be handled.
     */
    @Bean
    GoogleTokenServices tokenServices() {
        GoogleTokenServices services = new GoogleTokenServices();
        services.setCheckTokenEndpointUrl("https://www.googleapis.com/oauth2/v1/tokeninfo");
        services.setClientId("${google.client.id}");
        services.setClientSecret("${google.client.secret}");

        GoogleAccessTokenConverter tokenConverter = new GoogleAccessTokenConverter();
        tokenConverter.setUserTokenConverter(new DefaultUserAuthenticationConverter());
        services.setAccessTokenConverter(tokenConverter);

        return services;
    }

    private List<String> parseScopes(String commaSeparatedScopes) {
        List<String> scopes = new ArrayList<>();
        Collections.addAll(scopes, commaSeparatedScopes.split(","));
        return scopes;
    }

}
