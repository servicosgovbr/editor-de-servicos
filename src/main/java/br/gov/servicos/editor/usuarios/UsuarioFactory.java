package br.gov.servicos.editor.usuarios;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioFactory {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario criarUsuario(FormularioUsuario formulario) {
        String senhaCodificada = passwordEncoder.encode(formulario.getSenha());
        Papel papel = new Papel(Long.valueOf(formulario.getPapelId()));
        return new Usuario()
                .withCpf(formulario.getCpf())
                .withSenha(senhaCodificada)
                .withPapel(papel)
                .withSiorg(formulario.getSiorg())
                .withSiape(StringUtils.defaultIfEmpty(formulario.getSiape(), null))
                .withEmailInstitucional(formulario.getEmailInstitucional())
                .withEmailSecundario(formulario.getEmailSecundario())
                .withServidor(formulario.isServidor())
                .withHabilitado(true);
    }
}
