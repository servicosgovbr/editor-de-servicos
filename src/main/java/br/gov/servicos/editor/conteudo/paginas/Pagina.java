package br.gov.servicos.editor.conteudo.paginas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

import static lombok.AccessLevel.PRIVATE;

@Data
@Wither
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Pagina {
    String tipo = "orgao";
    String nome;
    String conteudo;
}
