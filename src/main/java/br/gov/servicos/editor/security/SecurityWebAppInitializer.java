package br.gov.servicos.editor.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.sql.DataSource;

import static org.springframework.http.HttpMethod.*;

public class SecurityWebAppInitializer extends WebSecurityConfigurerAdapter {

    public static final String LOGIN_URL = "/editar/autenticar";
    private PasswordEncoder passwordEncoder;
    private DataSource dataSource;

    public SecurityWebAppInitializer(PasswordEncoder passwordEncoder, DataSource dataSource) {
        this.passwordEncoder = passwordEncoder;
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_URL))
                .and()

                .formLogin()
                    .loginPage("/editar/autenticar")
                    .permitAll()
                .and()

                .logout()
                .logoutUrl("/editar/sair")
                .logoutSuccessUrl("/editar/autenticar?sair")

                .and()
                .authorizeRequests()
                .antMatchers("/editar/autenticar", "/editar/api/**").permitAll()
                .antMatchers(DELETE, "/editar/**").authenticated()
                .antMatchers(PATCH, "/editar/**").authenticated()
                .antMatchers(PUT, "/editar/**").authenticated()

                .anyRequest().fullyAuthenticated()

        ;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder);
    }

}
