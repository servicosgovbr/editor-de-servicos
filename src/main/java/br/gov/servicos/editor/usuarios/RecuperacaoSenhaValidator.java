package br.gov.servicos.editor.usuarios;

import br.com.caelum.stella.format.CPFFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class RecuperacaoSenhaValidator {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private CPFFormatter cpfFormatter = new CPFFormatter();

    public boolean isValid(FormularioRecuperarSenha formulario, TokenRecuperacaoSenha token, Usuario usuario) {
        return usuario.getCpf().equals(cpfFormatter.unformat(formulario.getCpf())) &&
                passwordEncoder.matches(formulario.getToken(), token.getToken());
    }
}
