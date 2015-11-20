package br.gov.servicos.editor.usuarios.cadastro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE)
@Wither
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CamposServidor {
    boolean servidor;

    @SiapeUnico
    String siape;
}
