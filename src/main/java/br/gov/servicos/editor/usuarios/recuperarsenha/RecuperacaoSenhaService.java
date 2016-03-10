package br.gov.servicos.editor.usuarios.recuperarsenha;

import br.gov.servicos.editor.usuarios.Usuario;
import br.gov.servicos.editor.usuarios.UsuarioInexistenteException;
import br.gov.servicos.editor.usuarios.UsuarioService;
import br.gov.servicos.editor.usuarios.token.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.lang.Long.valueOf;

@Component
public class RecuperacaoSenhaService {

    @Autowired
    private GeradorToken geradorToken;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenRepository tokens;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RecuperacaoSenhaValidator validator;

    @Value("${eds.max-tentativas-token}")
    private int maxTentativasToken;

    @Value("${eds.token.desabilitarUsuario}")
    private boolean desabilitarUsuarioAoCriarToken = true;

    private final Clock clock = Clock.systemUTC();

    public String gerarTokenParaUsuario(String usuarioId) {
        String token = geradorToken.gerar();
        Token tokenRecuperacaoSenha = new Token()
                .withUsuario(new Usuario().withId(valueOf(usuarioId)))
                .withDataCriacao(LocalDateTime.now(clock))
                .withTentativasSobrando(maxTentativasToken)
                .withToken(passwordEncoder.encode(token));

        tokens.deleteByUsuarioId(valueOf(usuarioId));
        tokens.save(tokenRecuperacaoSenha);

        if (desabilitarUsuarioAoCriarToken) {
            usuarioService.desabilitarUsuario(usuarioId);
        }

        return token;
    }

    public Usuario trocarSenha(FormularioRecuperarSenha formulario) throws CpfTokenInvalido, TokenExpirado {
        Token token = findLatestTokenByUsuarioId(formulario.getUsuarioId());
        Usuario usuario = token.getUsuario();

        Optional<TokenError> tokenError = validator.hasError(formulario, token);
        if (!tokenError.isPresent()) {
            Usuario usuarioComSenhaNova = usuario
                    .withSenha(passwordEncoder.encode(formulario.getCamposSenha().getSenha()))
                    .withHabilitado(true);

            usuarioService.save(usuarioComSenhaNova);
            tokens.delete(token.getId());

            return usuarioComSenhaNova;
        }

        if (tokenError.get().equals(TokenError.INVALIDO)) {
            Token novoToken = token.decrementarTentativasSobrando();
            tokens.save(novoToken);
            throw new CpfTokenInvalido(novoToken.getTentativasSobrando());
        }

        throw new TokenExpirado();
    }

    private Token findLatestTokenByUsuarioId(Long usuarioId) {
        Iterable<Token> tokensUsuario = tokens.findByUsuarioId(usuarioId);
        if (!tokensUsuario.iterator().hasNext()) {
            throw new UsuarioInexistenteException();
        }
        return tokensUsuario.iterator().next();
    }
}
