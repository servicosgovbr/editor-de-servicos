package br.gov.servicos.editor.usuarios;

import br.com.caelum.stella.format.CPFFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class RecuperacaoSenhaValidator {

    public final static String NOME = "TokenCpfValido";

    @Value("${eds.max-horas-token}")
    private Integer maxHorasToken;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Clock clock = Clock.systemUTC();

    private CPFFormatter cpfFormatter = new CPFFormatter();

    public Optional<TokenError> hasError(FormularioRecuperarSenha formulario, TokenRecuperacaoSenha token) {
        Usuario usuario = token.getUsuario();

        if(tokenExpirado(token)) {
            return Optional.of(TokenError.EXPIRADO);
        } else if(cpfETokenInvalido(formulario, token, usuario)) {
            return Optional.of(TokenError.INVALIDO);
        } else {
            return Optional.empty();
        }
    }

    private boolean tokenExpirado(TokenRecuperacaoSenha token) {
        return !(token.getDataCriacao().plusHours(maxHorasToken).isAfter(LocalDateTime.now(clock)));
    }

    private boolean cpfETokenInvalido(FormularioRecuperarSenha formulario, TokenRecuperacaoSenha token, Usuario usuario) {
        return !(usuario.getCpf().equals(cpfFormatter.unformat(formulario.getCpf())) &&
                passwordEncoder.matches(formulario.getToken(), token.getToken()));
    }
}
