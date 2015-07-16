package br.gov.servicos.editor.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import static br.gov.servicos.editor.usuarios.Papeis.*;

class SecurityConfig extends WebSecurityConfigurerAdapter {

    private PasswordEncoder passwordEncoder;

    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/editar/info").permitAll()
                .antMatchers("/editar/health").permitAll()
                .antMatchers("/editar/assets/**").permitAll()
                .antMatchers("/editar/fonts/**").permitAll()
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/editar/login").permitAll().and()
                .logout().logoutUrl("/editar/logout").logoutSuccessUrl("/editar/login").permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String hash = "46022771041f1486dc97b709a9bb35a3601ed9b7cbf82df729fcdf5319d48df0cad832848c6c3e82";
        String hash2 = "46022771041f14862063fa299c50f2ae429cd0f23dae153762bea01712bd2e6090e275097c355b74";

        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder)

                .withUser("mauricio.formiga@planejamento.gov.br").password(hash).roles(SUPER).and()
                .withUser("fabricio.fontenele@planejamento.gov.br").password(hash).roles(SUPER).and()

                .withUser("silvia.belarmino@planejamento.gov.br").password(hash).roles(ADMIN).and()
                .withUser("everson.aguiar@planejamento.gov.br").password(hash).roles(ADMIN).and()

                .withUser("cvillela@thoughtworks.com").password(hash).roles(PUBLICADOR).and()
                .withUser("oliviaj@thoughtworks.com").password(hash).roles(PUBLICADOR).and()
                .withUser("srosa@thoughtworks.com").password(hash).roles(PUBLICADOR).and()
                .withUser("jkirchne@thoughtworks.com").password(hash).roles(PUBLICADOR).and()

                // usuarios da oficina 16/jul

                .withUser("andrea.ricciardi@planejamento.gov.br").password(hash2).roles(PUBLICADOR).and()
                .withUser("joelson.vellozo@planejamento.gov.br").password(hash2).roles(PUBLICADOR).and()
                .withUser("bruno.palvarini@planejamento.gov.br").password(hash2).roles(PUBLICADOR).and()
                .withUser("izabel.garcia@planejamento.gov.br").password(hash2).roles(PUBLICADOR).and()
                .withUser("esau.mendes@planejamento.gov.br").password(hash2).roles(PUBLICADOR).and()
                .withUser("martins@aneel.gov.br").password(hash2).roles(PUBLICADOR).and()
                .withUser("alysson@aneel.gov.br").password(hash2).roles(PUBLICADOR).and()
                .withUser("monica.ribeiro@mds.gov.br").password(hash2).roles(PUBLICADOR).and()
                .withUser("ariana.souza@mds.gov.br").password(hash2).roles(PUBLICADOR).and()
                .withUser("nicir.chaves@previdencia.gov.br").password(hash2).roles(PUBLICADOR).and()
                .withUser("celia.torres@previdencia.gov.br").password(hash2).roles(PUBLICADOR).and()
                .withUser("matheus.foliveira@mj.gov.br").password(hash2).roles(PUBLICADOR).and()
                .withUser("lucas.fernandes@mj.gov.br").password(hash2).roles(PUBLICADOR).and()
                .withUser("raquel.sinfronio@mj.gov.br").password(hash2).roles(PUBLICADOR)
        ;
    }
}
