package br.gov.servicos.editor.conteudo;

import br.gov.servicos.editor.git.RepositorioGit;
import br.gov.servicos.editor.utils.EscritorDeArquivos;
import br.gov.servicos.editor.utils.LeitorDeArquivos;
import br.gov.servicos.editor.utils.ReformatadorXml;
import com.github.slugify.Slugify;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.Charset;

import static br.gov.servicos.editor.utils.Unchecked.Supplier.uncheckedSupplier;
import static com.google.common.io.Files.readFirstLine;
import static lombok.AccessLevel.PRIVATE;
import static org.eclipse.jgit.lib.Constants.MASTER;
import static org.eclipse.jgit.lib.Constants.R_HEADS;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class PaginaVersionada extends ConteudoVersionado<Pagina> {

    PaginaVersionada(String id, TipoPagina tipo, RepositorioGit repositorio, LeitorDeArquivos leitorDeArquivos, EscritorDeArquivos escritorDeArquivos, Slugify slugify, ReformatadorXml reformatadorXml) {
        super(id, repositorio, tipo, leitorDeArquivos, escritorDeArquivos, slugify, reformatadorXml);
    }

    protected Pagina getMetadadosConteudo() {
        File arquivo = getCaminhoAbsoluto().toFile();
        try {
            return getRepositorio().comRepositorioAbertoNoBranch(getBranchRef(),
                    uncheckedSupplier(() -> new Pagina()
                            .withNome(readFirstLine(arquivo, Charset.defaultCharset()))
                            .withTipo(getTipo().getNome())));
        } catch (Exception e) {
            return getRepositorio().comRepositorioAbertoNoBranch(R_HEADS + MASTER,
                    uncheckedSupplier(() -> new Pagina()
                            .withNome(readFirstLine(arquivo, Charset.defaultCharset()))
                            .withTipo(getTipo().getNome())));
        }
    }

}
