package br.gov.servicos.editor.usuarios;


import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@Wither
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode
public class FormularioUsuario {
    public static final String EMAIL_INVALIDO = "formato de email não valido";
    String cpf;
    @Size(min=8, max=50, message = "Senha: tamanho deve estar entre 8 e 50")
    String senha;
    String papelId;
    String siorg;
    String siape;
    @Email(message = "Email Institucional: " + EMAIL_INVALIDO)
    String emailInstitucional;
    @Email(message = "Email Secundario: " + EMAIL_INVALIDO)
    String emailSecundario;
    boolean servidor;

}
