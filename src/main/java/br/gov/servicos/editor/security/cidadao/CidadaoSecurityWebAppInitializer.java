package br.gov.servicos.editor.security.cidadao;

import br.gov.servicos.editor.conteudo.TipoPagina;
import br.gov.servicos.editor.security.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import static br.gov.servicos.editor.security.TipoPermissao.*;
import static org.springframework.http.HttpMethod.*;

@Order(2)
public class CidadaoSecurityWebAppInitializer extends WebSecurityConfigurerAdapter {


    private static final String LOGIN_CIDADAO_URL = "/editar/acesso-cidadao";

    private PreAuthenticatedAuthenticationProvider preAuthAuthenticationProvider;
    private AuthenticationSuccessHandler successHandler;
    private UserDetailsService cidadaoAuthenticationUserDetailsService;

    public CidadaoSecurityWebAppInitializer(PreAuthenticatedAuthenticationProvider preAuthAuthenticationProvider,
                                            AuthenticationSuccessHandler successHandler,
                                            UserDetailsService cidadaoAuthenticationUserDetailsService) {
        this.preAuthAuthenticationProvider = preAuthAuthenticationProvider;
        this.successHandler = successHandler;
        this.cidadaoAuthenticationUserDetailsService = cidadaoAuthenticationUserDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
        accessDeniedHandler.setErrorPage("/editar/acessoNegado");

        http
                .httpBasic()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_CIDADAO_URL)).authenticationDetailsSource(new CidadaoAuthenticationDetailsSource())
                .and()

                .formLogin()
                .loginPage("/editar/acesso-cidadao")
                .successHandler(successHandler)
                .permitAll()
                .and()

                .logout()
                .logoutUrl("/editar/sair")
                .logoutSuccessUrl("/editar/autenticar?sair")
                .deleteCookies("JSESSIONID", "SESSION")

                .and()

                .authorizeRequests()
                .antMatchers("/editar/autenticar", "/editar/api/ping", "/editar/acesso-cidadao").permitAll()
                .antMatchers(POST, "/editar/api/pagina/servico/*").hasAnyAuthority(EDITAR_SALVAR.comTipoPagina(TipoPagina.SERVICO))
                .anyRequest().authenticated()

                .and()
                    .exceptionHandling().accessDeniedHandler(accessDeniedHandler)

                .and()
                    .sessionManagement()
                    .invalidSessionUrl("/editar/autenticar?sessao");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(preAuthAuthenticationProvider)
                .userDetailsService(cidadaoAuthenticationUserDetailsService);
    }



}
