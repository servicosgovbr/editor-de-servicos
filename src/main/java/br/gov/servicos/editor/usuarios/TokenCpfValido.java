package br.gov.servicos.editor.usuarios;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RecuperacaoSenhaValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenCpfValido {
    String message() default "O CPF informado não é compatível com o cadastrado";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
