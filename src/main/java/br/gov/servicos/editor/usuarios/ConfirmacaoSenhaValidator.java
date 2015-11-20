package br.gov.servicos.editor.usuarios;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConfirmacaoSenhaValidator implements ConstraintValidator<ConfirmacaoSenha, CamposSenha> {
    @Override
    public void initialize(ConfirmacaoSenha constraintAnnotation) {
    }

    @Override
    public boolean isValid(CamposSenha value, ConstraintValidatorContext context) {
        return value.isValid();
    }
}
