package br.gov.servicos.editor.servicos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

import java.util.Optional;

@Data
@Wither
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Metadados {
    String id;

    Optional<Revisao> publicado;
    Optional<Revisao> editado;

    public boolean getTemAlteracoesNaoPublicadas() {
        return publicado.map(p -> editado.map(e -> e.getHorario().after(p.getHorario())).orElse(false)).orElse(false);
    }

    @JsonProperty("publicado")
    public Revisao getPublicadoOuNull() {
        return publicado.orElse(null);
    }

    @JsonProperty("editado")
    public Revisao getEditadoOuNull() {
        return publicado.orElse(null);
    }
}
