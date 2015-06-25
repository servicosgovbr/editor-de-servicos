package br.gov.servicos.editor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static br.gov.servicos.editor.usuarios.Papeis.ADMIN;
import static br.gov.servicos.editor.usuarios.Papeis.PUBLICADOR;
import static br.gov.servicos.editor.usuarios.Papeis.SUPER;

@Configuration
public class WebMVCConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/editar/login").setViewName("login");
    }

    @Bean
    public SecurityConfig securityConfig(PasswordEncoder passwordEncoder) {
        return new SecurityConfig(passwordEncoder);
    }

    private static class SecurityConfig extends WebSecurityConfigurerAdapter {

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
                    .loginPage("/editar/login")
                    .permitAll();

            http.logout()
                    .permitAll();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            String hash = "46022771041f1486dc97b709a9bb35a3601ed9b7cbf82df729fcdf5319d48df0cad832848c6c3e82";

            auth.inMemoryAuthentication()
                    .passwordEncoder(passwordEncoder)

                    .withUser("mauricio.formiga@planejamento.gov.br").password(hash).roles(SUPER).and()
                    .withUser("fabricio.fontenele@planejamento.gov.br").password(hash).roles(SUPER).and()
                    .withUser("silvia.belarmino@planejamento.gov.br").password(hash).roles(ADMIN).and()
                    .withUser("everson.aguiar@planejamento.gov.br").password(hash).roles(ADMIN).and()

                    .withUser("cvillela@thoughtworks.com").password(hash).roles(PUBLICADOR).and()
                    .withUser("oliviaj@thoughtworks.com").password(hash).roles(PUBLICADOR).and()
                    .withUser("srosa@thoughtworks.com").password(hash).roles(PUBLICADOR).and()
                    .withUser("jkirchne@thoughtworks.com").password(hash).roles(PUBLICADOR)
            ;
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
    }

}