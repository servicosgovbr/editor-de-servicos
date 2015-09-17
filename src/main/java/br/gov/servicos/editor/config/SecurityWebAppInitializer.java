package br.gov.servicos.editor.config;

import br.gov.servicos.editor.oauth2.google.GoogleTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.annotation.Resource;

import static java.util.Arrays.asList;
import static org.springframework.security.oauth2.common.AuthenticationScheme.form;
import static org.springframework.security.oauth2.common.AuthenticationScheme.query;

@Order(1)
@Configuration
@EnableOAuth2Client
@EnableWebSecurity
public class SecurityWebAppInitializer extends WebSecurityConfigurerAdapter {

    public static final String LOGIN_URL = "/editar/login";

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

    @Autowired
    OAuth2ClientContextFilter oauth2ClientContextFilter;

    @Autowired
    OAuth2ClientAuthenticationProcessingFilter oauth2ClientAuthenticationProcessingFilter;

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

        details.setUseCurrentUri(true);
        details.setAuthenticationScheme(query);
        details.setClientAuthenticationScheme(form);

        return details;
    }

    @Bean
    OAuth2ClientAuthenticationProcessingFilter oauth2ClientAuthenticationProcessingFilter(
            OAuth2RestTemplate restTemplate,
            GoogleTokenServices tokenServices
    ) {
        OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(LOGIN_URL);
        filter.setRestTemplate(restTemplate);
        filter.setTokenServices(tokenServices);
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_URL));

        http.addFilterAfter(oauth2ClientContextFilter, ExceptionTranslationFilter.class);
        http.addFilterBefore(oauth2ClientAuthenticationProcessingFilter, FilterSecurityInterceptor.class);

        http.logout().logoutUrl("/editar/logout").logoutSuccessUrl("/editar/");
        http.anonymous().disable();
        http.csrf().disable();

        http.authorizeRequests().anyRequest().fullyAuthenticated();
    }
}
