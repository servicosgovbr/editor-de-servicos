package br.gov.servicos.editor.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import static org.springframework.http.HttpMethod.*;

public class SecurityWebAppInitializer extends WebSecurityConfigurerAdapter {

    public static final String LOGIN_URL = "/editar/autenticar";
    private DaoAuthenticationProvider daoAuthenticationProvider;

    public SecurityWebAppInitializer(DaoAuthenticationProvider daoAuthenticationProvider) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
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
                    .deleteCookies("JSESSIONID", "SESSION")

                .and()
                .authorizeRequests()
                .antMatchers("/editar/autenticar", "/editar/api/**").permitAll()
                .antMatchers(DELETE, "/editar/**").authenticated()
                .antMatchers(PATCH, "/editar/**").authenticated()
                .antMatchers(PUT, "/editar/**").authenticated()

                .anyRequest().fullyAuthenticated()

                .and()
                    .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())

                .and()
                    .sessionManagement()
                    .invalidSessionUrl("/editar/autenticar?sessao");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider);
    }

}
