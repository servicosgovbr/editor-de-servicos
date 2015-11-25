package br.gov.servicos.editor.usuarios;

import br.gov.servicos.editor.usuarios.cadastro.TokenRecuperacaoSenhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

import static java.lang.Long.valueOf;

@Component
public class RecuperacaoSenhaService {

    @Autowired
    private GeradorToken geradorToken;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenRecuperacaoSenhaRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RecuperacaoSenhaValidator validator;

    private Clock clock = Clock.systemUTC();




    public String gerarTokenParaUsuario(String usuarioId) {
        String token = geradorToken.gerar();
        TokenRecuperacaoSenha tokenRecuperacaoSenha = new TokenRecuperacaoSenha()
                                                            .withUsuarioId(valueOf(usuarioId))
                                                            .withDataCriacao(LocalDateTime.now(clock))
                                                            .withToken(passwordEncoder.encode(token));
        repository.save(tokenRecuperacaoSenha);
        return token;
    }

    public boolean trocarSenha(FormularioRecuperarSenha formulario) {
        Long usuarioId = valueOf(formulario.getUsuarioId());
        TokenRecuperacaoSenha token = repository.findByUsuarioId(usuarioId);
        Usuario usuario = usuarioRepository.findById(usuarioId);

        if(validator.isValid(formulario, token, usuario)) {
            usuarioRepository.save(usuario.withSenha(passwordEncoder.encode(formulario.getCamposSenha().getSenha())));
            return true;
        } else {
            return false;
        }
    }
}
