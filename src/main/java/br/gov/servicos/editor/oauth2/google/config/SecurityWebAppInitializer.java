package br.gov.servicos.editor.oauth2.google.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

@Order(1)
@Configuration
@ImportResource({"classpath:security-context.xml"})
public class SecurityWebAppInitializer extends AbstractSecurityWebApplicationInitializer {
}
