package br.gov.servicos.editor.conteudo;

import br.gov.servicos.editor.conteudo.cartas.ServicoXML;
import br.gov.servicos.editor.frontend.Siorg;
import br.gov.servicos.editor.git.RepositorioGit;
import br.gov.servicos.editor.utils.EscritorDeArquivos;
import br.gov.servicos.editor.utils.LeitorDeArquivos;
import br.gov.servicos.editor.utils.ReformatadorXml;
import com.github.slugify.Slugify;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@NoArgsConstructor
@AllArgsConstructor
public class ConteudoVersionadoFactory {

    @Autowired
    Slugify slugify;

    @Autowired
    RepositorioGit repositorio;

    @Autowired
    LeitorDeArquivos leitorDeArquivos;

    @Autowired
    EscritorDeArquivos escritorDeArquivos;

    @Autowired
    ReformatadorXml reformatadorXml;

    @Autowired
    Siorg siorg;

    @Bean // necessário para @Cacheable
    @Scope("prototype")
    public ConteudoVersionado pagina(String texto, TipoPagina tipo) {
        return new ConteudoVersionado(slugify.slugify(texto), tipo, repositorio, leitorDeArquivos, escritorDeArquivos, slugify, reformatadorXml, siorg, obterDeserializador(tipo));
    }

    private DeserializadorConteudoXML obterDeserializador(TipoPagina tipo) {
        switch (tipo) {
            case SERVICO:
                return new DeserializadorConteudoXML(ServicoXML.class);
            case ORGAO:
                return new DeserializadorConteudoXML(OrgaoXML.class);
            case PAGINA_TEMATICA:
                return new DeserializadorConteudoXML(PaginaTematicaXML.class);
        }
        throw new IllegalArgumentException("Tipo inexistente: " + tipo);
    }
}