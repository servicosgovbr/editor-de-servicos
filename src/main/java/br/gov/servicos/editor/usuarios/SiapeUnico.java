package br.gov.servicos.editor.usuarios;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SiapeUnicoValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SiapeUnico {
    String message() default "SIAPE: já existe um usuário cadastrado com o mesmo SIAPE";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
