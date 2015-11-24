package br.gov.servicos.editor.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class RecuperacaoSenhaValidator {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean isValid(FormularioRecuperarSenha formulario, TokenRecuperacaoSenha token, Usuario usuario) {
        return usuario.getCpf().equals(formulario.getCpf()) &&
                passwordEncoder.matches(formulario.getToken(), token.getToken());
    }
}
