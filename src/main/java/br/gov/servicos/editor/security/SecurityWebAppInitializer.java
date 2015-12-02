package br.gov.servicos.editor.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import static org.springframework.http.HttpMethod.*;

public class SecurityWebAppInitializer extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_URL = "/editar/autenticar";
    private static final String API_DESPUBLICAR = "/editar/api/pagina/*/*/despublicar";
    private static final String API_DESCARTAR = "/editar/api/pagina/*/*/descartar";
    private static final String API_PUBLICAR = "/editar/api/pagina/**";
    private static final String API_SALVAR = "/editar/api/pagina/**";
    private static final String API_EXCLUIR = "/editar/api/pagina/**";
    private static final String PUBLICAR = "PUBLICAR";
    private static final String SALVAR = "SALVAR";
    private static final String DESPUBLICAR = "DESPUBLICAR";
    private static final String DESCARTAR = "DESCARTAR";
    private static final String EXCLUIR = "EXCLUIR";
    private DaoAuthenticationProvider daoAuthenticationProvider;
    private AuthenticationSuccessHandler successHandler;

    public SecurityWebAppInitializer(DaoAuthenticationProvider daoAuthenticationProvider,
                                     AuthenticationSuccessHandler successHandler) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.successHandler = successHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
        accessDeniedHandler.setErrorPage("/editar/acessoNegado");

        http
                .httpBasic()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_URL))
                .and()

                .formLogin()
                    .loginPage("/editar/autenticar")
                    .successHandler(successHandler)
                    .permitAll()
                .and()

                    .logout()
                    .logoutUrl("/editar/sair")
                    .logoutSuccessUrl("/editar/autenticar?sair")
                    .deleteCookies("JSESSIONID", "SESSION")

                .and()
                .authorizeRequests()
                .antMatchers("/editar/autenticar", "/editar/api/ping", "/editar/recuperar-senha").permitAll()
                .antMatchers(POST, API_DESPUBLICAR).hasAuthority(DESPUBLICAR)
                .antMatchers(POST, API_DESCARTAR).hasAuthority(DESCARTAR)
                .antMatchers(DELETE, API_EXCLUIR).hasAuthority(EXCLUIR)
                .antMatchers(PUT, API_PUBLICAR).hasAuthority(PUBLICAR)
                .antMatchers(POST, API_SALVAR).hasAnyAuthority(SALVAR, PUBLICAR, DESPUBLICAR)
                .antMatchers(DELETE, "/editar/**").authenticated()
                .antMatchers(PATCH, "/editar/**").authenticated()

                .anyRequest().fullyAuthenticated()

                .and()
                    .exceptionHandling().accessDeniedHandler(accessDeniedHandler)

                .and()
                    .sessionManagement()
                    .invalidSessionUrl("/editar/autenticar?sessao");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider);
    }

}
