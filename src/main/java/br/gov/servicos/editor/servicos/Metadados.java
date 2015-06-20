package br.gov.servicos.editor.servicos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import java.util.Date;

@Data
@Wither
@NoArgsConstructor
@AllArgsConstructor
public class Metadados {
    boolean novo;
    String versao;
    String revisao;
    String autor;
    Date horario;
}
