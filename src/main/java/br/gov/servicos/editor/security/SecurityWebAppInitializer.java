package br.gov.servicos.editor.security;

import br.gov.servicos.editor.conteudo.TipoPagina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import static br.gov.servicos.editor.security.TipoPermissao.*;
import static br.gov.servicos.editor.conteudo.TipoPagina.*;
import static org.springframework.http.HttpMethod.*;

public class SecurityWebAppInitializer extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_URL = "/editar/autenticar";
    private static final String API_DESPUBLICAR = "/editar/api/pagina/*/*/despublicar";
    private static final String API_DESCARTAR = "/editar/api/pagina/*/*/descartar";
    private static final String API_PAGINA = "/editar/api/pagina/**";
    private static final String API_NOVO_USUARIO = "/editar/usuarios/usuario";
    private static final String API_PAGINA_TEMATICA = "/editar/api/pagina/pagina-tematica/**";
    private static final String ADMIN = "ADMIN";
    private static final String PONTOFOCAL = "PONTOFOCAL";
    private static final String PUBLICADOR = "PUBLICADOR";
    private static final String EDITOR = "CADASTRO";
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
                .antMatchers(POST, API_DESPUBLICAR).hasAnyAuthority(DESPUBLICAR.getNome(), DESPUBLICAR.comOrgaoEspecifico())
                .antMatchers(DELETE, API_PAGINA_TEMATICA).hasAnyAuthority(EXCLUIR.comTipoPagina(PAGINA_TEMATICA))
                .antMatchers(PATCH, API_PAGINA_TEMATICA).hasAnyAuthority(RENOMEAR.comTipoPagina(PAGINA_TEMATICA))
                .antMatchers(PUT, API_PAGINA_TEMATICA).hasAnyAuthority(PUBLICAR.comTipoPagina(PAGINA_TEMATICA))
                .antMatchers(POST, API_PAGINA_TEMATICA).hasAnyAuthority(SALVAR.comTipoPagina(PAGINA_TEMATICA),
                                                                        PUBLICAR.comTipoPagina(PAGINA_TEMATICA),
                                                                        DESPUBLICAR.comTipoPagina(PAGINA_TEMATICA))
                .antMatchers(POST, API_DESCARTAR).hasAnyAuthority(DESCARTAR.getNome(), DESCARTAR.comOrgaoEspecifico())
                .antMatchers(DELETE, API_PAGINA).hasAnyAuthority(EXCLUIR.getNome(), EXCLUIR.comOrgaoEspecifico())
                .antMatchers(PATCH, API_PAGINA).hasAnyAuthority(RENOMEAR.getNome(), RENOMEAR.comOrgaoEspecifico())
                .antMatchers(PUT, API_PAGINA).hasAnyAuthority(PUBLICAR.getNome(), PUBLICAR.comOrgaoEspecifico())
                .antMatchers(POST, API_PAGINA).hasAnyAuthority(SALVAR.getNome(), PUBLICAR.getNome(), DESPUBLICAR.getNome())
                .antMatchers(GET, API_NOVO_USUARIO).hasAnyAuthority(CADASTRAR.comPapel(ADMIN),
                                                                 CADASTRAR.comPapel(PONTOFOCAL),
                                                                 CADASTRAR.comPapel(PUBLICADOR),
                                                                 CADASTRAR.comPapel(EDITOR))
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
