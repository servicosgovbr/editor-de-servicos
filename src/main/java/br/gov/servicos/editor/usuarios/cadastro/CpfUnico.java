package br.gov.servicos.editor.usuarios.cadastro;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CpfUnicoValidator.class)
@Target( { ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CpfUnico {
    String campoMarcaSeValidacaoHabilitada();

    String nomeCampoComValorCpf();

    String message() default "CPF: já existe um usuário cadastrado com o mesmo CPF";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
