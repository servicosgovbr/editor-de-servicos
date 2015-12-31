package br.gov.servicos.editor.usuarios;

import br.com.caelum.stella.bean.validation.CPF;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@Wither
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class FormularioAcessoCidadao {

    public static final String CAMPO_OBRIGATORIO = "campo obrigatório";

    @NotBlank(message = "Nome: " + CAMPO_OBRIGATORIO)
    String nome;

    @NotBlank(message = "Nome: " + CAMPO_OBRIGATORIO)
    @Email(message = "Email: Formato não é válido")
    String email;

    @NotBlank(message = "CPF: " + CAMPO_OBRIGATORIO)
    @CPF(message = "CPF: não é valido")
    String cpf;
}
