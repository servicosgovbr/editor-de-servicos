package br.gov.servicos.editor.usuarios;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ServidorValidator implements ConstraintValidator<Servidor, CamposServidor> {
    @Override
    public void initialize(Servidor constraintAnnotation) {

    }

    @Override
    public boolean isValid(CamposServidor camposServidor, ConstraintValidatorContext context) {
        return !camposServidor.isServidor() || !camposServidor.getSiape().isEmpty();
    }
}
