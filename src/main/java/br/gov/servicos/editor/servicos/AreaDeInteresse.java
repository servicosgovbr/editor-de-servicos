package br.gov.servicos.editor.servicos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import java.util.Optional;

@Data
@Wither
@NoArgsConstructor
@AllArgsConstructor
public class AreaDeInteresse {
    String id;
    String area;
    String subArea;

    public String getSubArea() {
        return Optional
                .ofNullable(subArea)
                .orElse("");
    }
}
