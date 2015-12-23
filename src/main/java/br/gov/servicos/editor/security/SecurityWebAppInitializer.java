package br.gov.servicos.editor.security;

import br.gov.servicos.editor.conteudo.TipoPagina;
import br.gov.servicos.editor.security.cidadao.CidadaoAuthenticationDetailsSource;
import br.gov.servicos.editor.security.cidadao.CidadaoPreAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import static br.gov.servicos.editor.security.TipoPermissao.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
public class SecurityWebAppInitializer extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_URL = "/editar/autenticar";
    private static final String LOGIN_CIDADAO_URL = "/editar/acesso-cidadao";

    private static final String API_NOVO_USUARIO = "/editar/usuarios/usuario";
    private static final String ADMIN = "ADMIN";
    private static final String PONTOFOCAL = "PONTOFOCAL";
    private static final String PUBLICADOR = "PUBLICADOR";
    private static final String EDITOR = "CADASTRO";

    private static final String API_DESPUBLICAR_PATTERN = "/editar/api/pagina/%s/*/despublicar";
    private static final String API_DESCARTAR_PATTERN = "/editar/api/pagina/%s/*/descartar";
    private static final String API_PAGINA_PATTERN = "/editar/api/pagina/%s/*";

    @Autowired
    private DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private UserDetailsService editorUserDetailsService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
        accessDeniedHandler.setErrorPage("/editar/acessoNegado");


        HttpSecurity httpSecurityBuilder  = http
                .httpBasic()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_URL))
                .and()

                .formLogin()
                .loginPage(LOGIN_URL)
                .successHandler(successHandler)
                .permitAll()
                .and()

                .logout()
                .logoutUrl("/editar/sair")
                .logoutSuccessUrl("/editar/autenticar?sair")
                .deleteCookies("JSESSIONID", "SESSION")

                .and()

                .authorizeRequests()
                .antMatchers("/editar/autenticar", "/editar/api/ping", "/editar/recuperar-senha", "/editar/acesso-cidadao").permitAll()
                .and();

        // este laço irá adicionar todas as permissões específicas por página
        for (TipoPagina tipoPagina: TipoPagina.values()) {
              httpSecurityBuilder.authorizeRequests()
                      .antMatchers(DELETE, constroiURLTipoPagina(API_PAGINA_PATTERN, tipoPagina)).hasAnyAuthority(EXCLUIR.comTipoPagina(tipoPagina), EXCLUIR.comTipoPaginaParaOrgaoEspecifico(tipoPagina))
                      .antMatchers(PATCH, constroiURLTipoPagina(API_PAGINA_PATTERN, tipoPagina)).hasAnyAuthority(CRIAR.comTipoPagina(tipoPagina), CRIAR.comTipoPaginaParaOrgaoEspecifico(tipoPagina))
                      .antMatchers(PUT, constroiURLTipoPagina(API_PAGINA_PATTERN, tipoPagina)).hasAnyAuthority(PUBLICAR.comTipoPagina(tipoPagina), PUBLICAR.comTipoPaginaParaOrgaoEspecifico(tipoPagina))
                      .antMatchers(POST, constroiURLTipoPagina(API_PAGINA_PATTERN, tipoPagina)).hasAnyAuthority(EDITAR_SALVAR.comTipoPagina(tipoPagina), EDITAR_SALVAR.comTipoPaginaParaOrgaoEspecifico(tipoPagina))
                      .antMatchers(POST, constroiURLTipoPagina(API_DESPUBLICAR_PATTERN, tipoPagina)).hasAnyAuthority(DESPUBLICAR.comTipoPagina(tipoPagina), DESPUBLICAR.comTipoPaginaParaOrgaoEspecifico(tipoPagina))
                      .antMatchers(POST, constroiURLTipoPagina(API_DESCARTAR_PATTERN, tipoPagina)).hasAnyAuthority(DESCARTAR.comTipoPagina(tipoPagina),DESCARTAR.comTipoPaginaParaOrgaoEspecifico(tipoPagina))
            .and();
        }

         httpSecurityBuilder.authorizeRequests()
                .antMatchers(GET, API_NOVO_USUARIO).hasAnyAuthority(CADASTRAR.comPapel(ADMIN),
                                                                 CADASTRAR.comPapel(PONTOFOCAL),
                                                                 CADASTRAR.comPapel(PUBLICADOR),
                                                                 CADASTRAR.comPapel(EDITOR))
                .antMatchers(POST, API_NOVO_USUARIO).hasAnyAuthority(CADASTRAR.comPapel(ADMIN),
                                                                  CADASTRAR.comPapel(PONTOFOCAL),
                                                                  CADASTRAR.comPapel(PUBLICADOR),
                                                                  CADASTRAR.comPapel(EDITOR))

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
                .authenticationProvider(daoAuthenticationProvider)
                .userDetailsService(editorUserDetailsService);
    }

    private String constroiURLTipoPagina(String urlPattern, TipoPagina tipoPagina) {
        return String.format(urlPattern, tipoPagina.getNome());
    }


}
