package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.security.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
class Ping {
    UserProfile profile;
    Long horario;
}
