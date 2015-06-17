package br.gov.servicos.editor.servicos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

@Data
@Wither
@NoArgsConstructor
@AllArgsConstructor
public class TempoEstimado {

    String tipo; // 'entre' ou 'até'

    String entreMinimo;
    String entreTipoMinimo; // minutos, horas, etc

    String ateMaximo;
    String ateTipoMaximo; // minutos, horas, etc

    String entreMaximo;
    String entreTipoMaximo; // minutos, horas, etc

    String excecoes;
}
