package br.gov.servicos.editor.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioFactory {
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario criarUsuario(FormularioUsuario formulario) {
        String senhaCodificada = passwordEncoder.encode(formulario.getPassword());
        Papel papel = new Papel(Long.valueOf(formulario.getPapelId()));
        return new Usuario(formulario.getCpf(), senhaCodificada, papel);
    }
}
