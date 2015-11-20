package br.gov.servicos.editor.usuarios.cadastro;


import br.gov.servicos.editor.usuarios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static net.logstash.logback.encoder.org.apache.commons.lang.StringUtils.isBlank;

public class SiapeUnicoValidator implements ConstraintValidator<SiapeUnico, String> {
    @Autowired
    private UsuarioRepository repository;

    @Override
    public void initialize(SiapeUnico constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return isBlank(value) || repository.findBySiape(value) == null;
    }
}
