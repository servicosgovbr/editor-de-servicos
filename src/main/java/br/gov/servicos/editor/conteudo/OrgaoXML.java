package br.gov.servicos.editor.conteudo;

import br.gov.servicos.editor.frontend.Siorg;
import br.gov.servicos.editor.git.ConteudoMetadados;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

import javax.xml.bind.annotation.XmlType;

import static br.gov.servicos.editor.conteudo.TipoPagina.ORGAO;
import static lombok.AccessLevel.PRIVATE;

@Data
@Wither
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@XmlType(name = "Orgao")
public class OrgaoXML implements ConteudoMetadadosProvider {

    String url;

    String nome;

    String conteudo;

    String contato;

    @Override
    @SneakyThrows
    public ConteudoMetadados toConteudoMetadados(Siorg siorg) {
        return new ConteudoMetadados()
                .withTipo(ORGAO.getNome())
                .withNome(getNome())
                .withNomeOrgao(getNome())
                .withOrgaoId(url);
    }
}
