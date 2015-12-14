package br.gov.servicos.editor.git;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

@Data
@Wither
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConteudoMetadados {
    String tipo;
    String nome;
    String nomeOrgao = " — — ";
    String orgaoId = "";
}
