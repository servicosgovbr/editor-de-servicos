package br.gov.servicos.editor.conteudo.paginas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.cartas.Carta;
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

    @Bean // necessário para @Cacheable
    @Scope("prototype")
    public ConteudoVersionado pagina(String texto, TipoPagina tipo) {
        if (tipo == TipoPagina.SERVICO)
            return new Carta(slugify.slugify(texto), repositorio, leitorDeArquivos, escritorDeArquivos, slugify, reformatadorXml);
        return new PaginaVersionada(slugify.slugify(texto), tipo, repositorio, leitorDeArquivos, escritorDeArquivos, slugify, reformatadorXml);
    }

}