package br.gov.servicos.editor.usuarios;

import br.gov.servicos.editor.usuarios.cadastro.TokenRecuperacaoSenhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TokenRecuperacaoSenhaService {

    public static final int TOKEN_LENGHT = 50;

    @Autowired
    private GeradorToken geradorToken;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenRecuperacaoSenhaRepository repository;

    public String gerarParaUsuario(String cpf) {
        String token = geradorToken.gerar();
        TokenRecuperacaoSenha tokenRecuperacaoSenha = new TokenRecuperacaoSenha()
                                                            .withCpf(cpf)
                                                            .withToken(passwordEncoder.encode(token));
        repository.save(tokenRecuperacaoSenha);
        return token;
    }
}
