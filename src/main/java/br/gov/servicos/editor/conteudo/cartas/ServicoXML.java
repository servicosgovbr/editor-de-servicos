package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoMetadadosProvider;
import br.gov.servicos.editor.frontend.Siorg;
import br.gov.servicos.editor.git.ConteudoMetadados;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Optional;

import static javax.xml.bind.annotation.XmlAccessType.NONE;
import static lombok.AccessLevel.PRIVATE;

@Data
@Wither
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@XmlAccessorType(NONE)
@XmlType(name = "Servico", propOrder = {"nome", "orgao"})
public class ServicoXML implements ConteudoMetadadosProvider {
    String tipo = "servico";

    @XmlElement(required = true)
    String nome;

    @XmlElement(required = true)
    TagOrgao orgao;

    @Override
    public ConteudoMetadados toConteudoMetadados(Siorg siorg) {
        String id = Optional.ofNullable(getOrgao())
                .map(TagOrgao::getId)
                .orElse("");
        String nomeOrgao = nomeOrgao(siorg, id);

        return new ConteudoMetadados()
                .withTipo(tipo)
                .withNome(getNome())
                .withNomeOrgao(nomeOrgao)
                .withOrgaoId(id);
    }

    private String nomeOrgao(Siorg siorg, String idOrgao) {
        return siorg.nomeDoOrgao(idOrgao)
                .orElse(" - - ");
    }

    @Data
    @Wither
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = PRIVATE)
    @XmlAccessorType(NONE)
    @XmlType(name = "Orgao", propOrder = {"id"})
    public static class TagOrgao {
        @XmlAttribute(required = true)
        String id;
    }

}