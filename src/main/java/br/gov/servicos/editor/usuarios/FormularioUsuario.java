package br.gov.servicos.editor.usuarios;


import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;
import org.hibernate.validator.constraints.Email;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@Wither
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode
public class FormularioUsuario {
    public static final String EMAIL_INVALIDO = "Formato de email não valido";
    String cpf;
    String senha;
    String papelId;
    String siorg;
    String siape;
    @Email(message = EMAIL_INVALIDO)
    String emailInstitucional;
    @Email(message = EMAIL_INVALIDO)
    String emailSecundario;
    boolean servidor;

}
