package br.gov.servicos.editor.usuarios.cadastro;


import br.gov.servicos.editor.usuarios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CpfUnicoValidator implements ConstraintValidator<CpfUnico, String> {
    @Autowired
    private UsuarioRepository repository;

    @Override
    public void initialize(CpfUnico constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return repository.findByCpf(value) == null;
    }
}
