package br.gov.servicos.editor.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import static org.springframework.http.HttpMethod.*;

public class SecurityWebAppInitializer extends WebSecurityConfigurerAdapter {

    public static final String LOGIN_URL = "/editar/autenticar";
    private PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityWebAppInitializer(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
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
                .csrf().disable()
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
        String hash = "$2a$10$1O.BjADPpzYc2qm6c27U8ucMfZEhhHUALb/4TjiQMMbjoRgIqqizm";
        auth
                 .inMemoryAuthentication()
                .passwordEncoder(passwordEncoder)
                .withUser("mauricio.formiga@planejamento.gov.br").password(hash).roles("ADMIN").and()
                .withUser("formiga.mauricio@gmail.com").password(hash).roles("ADMIN").and()
                .withUser("almeidafab@gmail.com").password(hash).roles("ADMIN").and()
                .withUser("fabricio.fontenele@planejamento.gov.br").password(hash).roles("ADMIN").and()
                .withUser("carlos.vieira@planejamento.gov.br").password(hash).roles("ADMIN").and()
                .withUser("silvia.belarmino@planejamento.gov.br").password(hash).roles("ADMIN").and()
                .withUser("andrea.ricciardi@planejamento.gov.br").password(hash).roles("ADMIN").and()
                .withUser("everson.aguiar@planejamento.gov.br").password(hash).roles("ADMIN").and()
                .withUser("joelson.vellozo@planejamento.gov.br").password(hash).roles("ADMIN").and()
                .withUser("izabel.garcia@planejamento.gov.br").password(hash).roles("ADMIN").and()
                .withUser("cvillela@thoughtworks.com").password(hash).roles("ADMIN").and()
                .withUser("bleite@thoughtworks.com").password(hash).roles("ADMIN").and()
                .withUser("oliviaj@thoughtworks.com").password(hash).roles("ADMIN").and()
                .withUser("srosa@thoughtworks.com").password(hash).roles("ADMIN").and()
                .withUser("jkirchne@thoughtworks.com").password(hash).roles("ADMIN").and()
                .withUser("pleal@thoughtworks.com").password(hash).roles("ADMIN").and()
                .withUser("gfreita@thoughtworks.com").password(hash).roles("ADMIN").and()
                .withUser("gramos@thoughtworks.com").password(hash).roles("ADMIN").and()
                .withUser("nitai.silva@cultura.gov.br").password(hash).roles("ADMIN").and()
                .withUser("esau.mendes@planejamento.gov.br").password(hash).roles("ADMIN").and()
                .withUser("thiago.avila@seplag.al.gov.br").password(hash).roles("ADMIN").and()
                .withUser("toni.esteves@gmail.com").password(hash).roles("ADMIN");
    }

}
