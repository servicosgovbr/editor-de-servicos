package br.gov.servicos.editor.usuarios.cadastro;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ConfirmacaoSenhaValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfirmacaoSenha {
    String message() default "Confirmação Senha: as senhas estão diferentes";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
