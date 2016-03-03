package br.gov.servicos.editor.usuarios;

import br.com.caelum.stella.bean.validation.CPF;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @NotBlank(message = "Nome: campo obrigatório")
    String nome;

    @NotBlank(message = "Email: campo obrigatório")
    @Email(message = "Email: formato não é válido")
    String email;

    @NotBlank(message = "CPF: campo obrigatório")
    @CPF(message = "CPF: não é valido")
    String cpf;
}
