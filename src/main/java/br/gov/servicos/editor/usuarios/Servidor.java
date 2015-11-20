package br.gov.servicos.editor.usuarios;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ServidorValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Servidor {
    String message() default "SIAPE: campo obrigatorio para servidores";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
