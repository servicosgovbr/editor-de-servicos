package br.gov.servicos.editor.frontend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
class Ping {
    String login;
    Long horario;
}
