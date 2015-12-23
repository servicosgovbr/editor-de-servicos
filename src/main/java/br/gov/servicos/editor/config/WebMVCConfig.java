package br.gov.servicos.editor.config;

import br.gov.servicos.editor.security.CidadaoAuthenticationUserDetailsService;
import br.gov.servicos.editor.security.CustomLoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.util.List;

@Configuration
public class WebMVCConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new SourceHttpMessageConverter<>());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new CustomLoginSuccessHandler("/editar", "/editar/autenticar");
    }


    @Bean
    public AuthenticationSuccessHandler cidadaoSuccessHandler() {
        return new CustomLoginSuccessHandler("/editar","/editar/acesso-cidadao");
    }

    @Bean
    public YamlPropertiesFactoryBean yamlPropertiesFactoryBean() throws IOException {
        YamlPropertiesFactoryBean permissoes = new YamlPropertiesFactoryBean();
        permissoes.setResources(new ClassPathResource("permissoes.yaml"));
        permissoes.afterPropertiesSet();
        return permissoes;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasenames("GerenciarUsuarios");
        resourceBundleMessageSource.setDefaultEncoding("UTF-8");
        return resourceBundleMessageSource;
    }
}
