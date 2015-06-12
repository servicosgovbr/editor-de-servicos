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

    String minimo;
    String tipoMinimo; // minutos, horas, etc

    String maximo;
    String tipoMaximo; // minutos, horas, etc

    String excecoes;
}
