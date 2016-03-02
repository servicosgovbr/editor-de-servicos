package br.gov.servicos.editor.usuarios.cadastro;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CidadaoValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cidadao {
    String message() default "SIAPE: campo permitido apenas para servidores";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
