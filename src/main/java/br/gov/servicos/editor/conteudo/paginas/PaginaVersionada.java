package br.gov.servicos.editor.conteudo.paginas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.RepositorioGit;
import br.gov.servicos.editor.utils.EscritorDeArquivos;
import br.gov.servicos.editor.utils.LeitorDeArquivos;
import br.gov.servicos.editor.utils.ReformatadorXml;
import com.github.slugify.Slugify;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import static br.gov.servicos.editor.utils.Unchecked.Supplier.uncheckedSupplier;
import static com.google.common.io.Files.readFirstLine;
import static lombok.AccessLevel.PRIVATE;
import static org.eclipse.jgit.lib.Constants.MASTER;
import static org.eclipse.jgit.lib.Constants.R_HEADS;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PaginaVersionada extends ConteudoVersionado<Pagina> {

    @Getter
    String id;

    TipoPagina tipo;

    PaginaVersionada(String id, TipoPagina tipo, RepositorioGit repositorio, LeitorDeArquivos leitorDeArquivos, EscritorDeArquivos escritorDeArquivos, Slugify slugify, ReformatadorXml reformatadorXml) {
        super(repositorio, leitorDeArquivos, escritorDeArquivos, slugify, reformatadorXml);
        this.id = id;
        this.tipo = tipo;
    }

    @Override
    public Path getCaminho() {
        return Paths.get("conteudo", tipo.getNomePasta(), id + ".md");
    }

    public Pagina getConteudo() {
        File arquivo = getCaminhoAbsoluto().toFile();
        try {
            return getRepositorio().comRepositorioAbertoNoBranch(getBranchRef(),
                    uncheckedSupplier(() -> new Pagina().withNome(readFirstLine(arquivo, Charset.defaultCharset()))));
        } catch (Exception e) {
            return getRepositorio().comRepositorioAbertoNoBranch(R_HEADS + MASTER,
                    uncheckedSupplier(() -> new Pagina().withNome(readFirstLine(arquivo, Charset.defaultCharset()))));
        }
    }

}
