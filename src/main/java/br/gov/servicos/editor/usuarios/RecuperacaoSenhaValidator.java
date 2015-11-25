package br.gov.servicos.editor.usuarios;

import br.com.caelum.stella.format.CPFFormatter;
import br.gov.servicos.editor.usuarios.cadastro.SiapeUnico;
import br.gov.servicos.editor.usuarios.cadastro.TokenRecuperacaoSenhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.lang.Long.valueOf;

@Component
public class RecuperacaoSenhaValidator implements ConstraintValidator<TokenCpfValido, CamposVerificacaoRecuperarSenha> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenRecuperacaoSenhaRepository repository;

    private CPFFormatter cpfFormatter = new CPFFormatter();

    @Override
    public void initialize(TokenCpfValido constraintAnnotation) {
    }

    @Override
    public boolean isValid(CamposVerificacaoRecuperarSenha value, ConstraintValidatorContext context) {
        TokenRecuperacaoSenha token = repository.findByUsuarioId(valueOf(value.getUsuarioId()));
        Usuario usuario = token.getUsuario();
        return usuario.getCpf().equals(cpfFormatter.unformat(value.getCpf())) &&
                passwordEncoder.matches(value.getToken(), token.getToken());
    }
}
