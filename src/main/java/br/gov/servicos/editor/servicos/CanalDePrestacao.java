package br.gov.servicos.editor.servicos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

@Data
@Wither
@NoArgsConstructor
@AllArgsConstructor
public class CanalDePrestacao {
    String tipo;
    String caption;
    String referencia;
    Boolean preferencial;
}
