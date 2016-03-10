package br.gov.servicos.editor.usuarios.recuperarsenha;

import br.com.caelum.stella.format.CPFFormatter;
import br.gov.servicos.editor.usuarios.Usuario;
import br.gov.servicos.editor.usuarios.token.Token;
import br.gov.servicos.editor.usuarios.token.TokenError;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(level = PRIVATE)
public class RecuperacaoSenhaValidator {

    @Value("${eds.max-horas-token}")
    Integer maxHorasToken;

    @Autowired
    PasswordEncoder passwordEncoder;

    final Clock clock = Clock.systemUTC();

    final CPFFormatter cpfFormatter = new CPFFormatter();

    public Optional<TokenError> hasError(FormularioRecuperarSenha formulario, Token token) {
        Usuario usuario = token.getUsuario();

        if (tokenExpirado(token)) {
            return Optional.of(TokenError.EXPIRADO);
        }

        if (cpfETokenInvalido(formulario, token, usuario)) {
            return Optional.of(TokenError.INVALIDO);
        }

        return Optional.empty();
    }

    private boolean tokenExpirado(Token token) {
        return !token.getDataCriacao().plusHours(maxHorasToken).isAfter(LocalDateTime.now(clock));
    }

    private boolean cpfETokenInvalido(FormularioRecuperarSenha formulario, Token token, Usuario usuario) {
        return !(usuario.getCpf().equals(cpfFormatter.unformat(formulario.getCpf())) &&
                passwordEncoder.matches(formulario.getToken(), token.getToken()));
    }
}
