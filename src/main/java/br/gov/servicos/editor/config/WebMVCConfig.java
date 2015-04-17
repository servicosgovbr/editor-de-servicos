package br.gov.servicos.editor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static br.gov.servicos.editor.usuarios.Papeis.ADMINISTRADOR;
import static br.gov.servicos.editor.usuarios.Papeis.USUARIO;

@Configuration
public class WebMVCConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @Bean
    public SecurityConfig securityConfig() {
        return new SecurityConfig();
    }

    private static class SecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/assets/**").permitAll()
                    .antMatchers("/fonts/**").permitAll()
                    .anyRequest().authenticated();

            http.formLogin()
                    .loginPage("/login")
                    .permitAll();

            http.logout()
                    .permitAll();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                    .withUser("admin").password("admin").roles(ADMINISTRADOR).and()
                    .withUser("user").password("user").roles(USUARIO);
        }
    }
}
