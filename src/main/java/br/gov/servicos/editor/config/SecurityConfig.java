package br.gov.servicos.editor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static br.gov.servicos.editor.usuarios.Papeis.ADMINISTRADOR;
import static br.gov.servicos.editor.usuarios.Papeis.USUARIO;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password("admin").roles(ADMINISTRADOR).and()
                .withUser("user").password("user").roles(USUARIO);
    }
}
