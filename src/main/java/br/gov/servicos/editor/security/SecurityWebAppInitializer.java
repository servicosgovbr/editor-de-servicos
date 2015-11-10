package br.gov.servicos.editor.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import static org.springframework.http.HttpMethod.*;

@Order(1)
@Configuration
@EnableWebMvcSecurity
public class SecurityWebAppInitializer extends WebSecurityConfigurerAdapter {

    public static final String LOGIN_URL = "/editar/autenticar";

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
                .antMatchers("/login", "/editar/autenticar").permitAll()
                .antMatchers(DELETE, "/editar/**").authenticated()
                .antMatchers(PATCH, "/editar/**").authenticated()
                .antMatchers(PUT, "/editar/**").authenticated()

                .anyRequest().fullyAuthenticated()

        ;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                 .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER").and()
                .withUser("jkirchne@thoughtworks.com").password("password").roles("ADMIN").and()
                .withUser("jean.kirchner@gmail.com").password("password").roles("ADMIN").and()
                .withUser("ojanequi@thoughtworks.com").password("password").roles("ADMIN").and()
                .withUser("srosa@thoughtworks.com").password("password").roles("ADMIN").and()
                .withUser("bleite@thoughtworks.com").password("password").roles("ADMIN").and()
                .withUser("gfreita@thoughtworks.com").password("password").roles("ADMIN").and()

                .withUser("almeidafab@gmail.com").password("password").roles("ADMIN").and()
                .withUser("formiga.mauricio@gmail.com").password("password").roles("ADMIN");
    }

}
