package br.gov.servicos.editor.usuarios;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CidadaoValidator implements ConstraintValidator<Cidadao, CamposServidor> {
    @Override
    public void initialize(Cidadao constraintAnnotation) {

    }

    @Override
    public boolean isValid(CamposServidor camposServidor, ConstraintValidatorContext context) {
        return camposServidor.isServidor() || camposServidor.getSiape().isEmpty();
    }
}
