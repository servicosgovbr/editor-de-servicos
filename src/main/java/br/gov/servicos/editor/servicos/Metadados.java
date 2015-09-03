package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.cartas.Carta;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

import java.io.Serializable;

@Data
@Wither
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Metadados implements Serializable {
    String id;

    Revisao publicado;
    Revisao editado;

    Carta.Servico servico;

    public boolean getTemAlteracoesNaoPublicadas() {
        return publicado != null && editado != null && editado.getHorario().after(publicado.getHorario());
    }
}
