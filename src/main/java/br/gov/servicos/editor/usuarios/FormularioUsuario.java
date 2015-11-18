package br.gov.servicos.editor.usuarios;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@Wither
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class FormularioUsuario {
    String cpf;
    String senha;
    String papelId;
    String siorg;
    String siape;
    String emailInstitucional;
    String emailSecundario;
    boolean servidor;

}
