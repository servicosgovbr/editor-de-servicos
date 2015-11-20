package br.gov.servicos.editor.usuarios;


import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SiapeUnicoValidator implements ConstraintValidator<SiapeUnico, String> {
    @Autowired
    private UsuarioRepository repository;

    @Override
    public void initialize(SiapeUnico constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.isEmpty() || repository.findBySiape(value) == null;
    }
}
