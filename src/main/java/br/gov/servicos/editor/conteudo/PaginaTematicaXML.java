package br.gov.servicos.editor.conteudo;

import br.gov.servicos.editor.frontend.Siorg;
import br.gov.servicos.editor.git.ConteudoMetadados;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

import static br.gov.servicos.editor.conteudo.TipoPagina.PAGINA_TEMATICA;
import static lombok.AccessLevel.PRIVATE;

@Data
@Wither
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class PaginaTematicaXML implements ConteudoMetadadosProvider {
    String nome;
    String conteudo;

    @Override
    @SneakyThrows
    public ConteudoMetadados toConteudoMetadados(Siorg siorg) {
        return new ConteudoMetadados()
                .withTipo(PAGINA_TEMATICA.getNome())
                .withNome(this.getNome());
    }
}
