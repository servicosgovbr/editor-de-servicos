package br.gov.servicos.editor.fixtures;

import br.gov.servicos.editor.conteudo.paginas.FormatadorConteudoPagina;
import br.gov.servicos.editor.conteudo.paginas.Pagina;
import br.gov.servicos.editor.conteudo.paginas.TipoPagina;
import br.gov.servicos.editor.utils.EscritorDeArquivos;
import br.gov.servicos.editor.utils.Unchecked;
import lombok.experimental.FieldDefaults;
import org.eclipse.jgit.api.Git;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static br.gov.servicos.editor.conteudo.paginas.TipoPagina.*;
import static br.gov.servicos.editor.utils.Unchecked.Supplier.*;
import static java.util.Arrays.asList;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(makeFinal = true, level = PRIVATE)
public class RepositorioCartasBuilder {

    Path localRepositorio;

    Map<Path, String> paginas;
    FormatadorConteudoPagina formatadorPagina;

    public RepositorioCartasBuilder(Path localRepositorio) {
        this.localRepositorio = localRepositorio;
        paginas = new HashMap<>();
        formatadorPagina = new FormatadorConteudoPagina();
    }

    public RepositorioCartasBuilder touchCarta(String id) {
        return carta(id, "");
    }

    public RepositorioCartasBuilder carta(String id, String conteudo) {
        return pagina(SERVICO, id, conteudo);
    }

    public RepositorioCartasBuilder touchOrgao(String id) {
        return orgao(id, "");
    }

    public RepositorioCartasBuilder orgao(String id, String conteudo) {
        return pagina(ORGAO, id, conteudo);
    }

    public RepositorioCartasBuilder orgao(String id, Pagina conteudo) {
        return pagina(ORGAO, id, conteudo);
    }

    public RepositorioCartasBuilder touchAreaDeInteresse(String id) {
        return areaDeInteresse(id, "");
    }

    public RepositorioCartasBuilder areaDeInteresse(String id, String conteudo) {
        return pagina(AREA_DE_INTERESSE, id, conteudo);
    }

    public RepositorioCartasBuilder areaDeInteresse(String id, Pagina conteudo) {
        return pagina(AREA_DE_INTERESSE, id, conteudo);
    }

    public RepositorioCartasBuilder touchPaginaEspecial(String id) {
        return paginaEspecial(id, "");
    }

    public RepositorioCartasBuilder paginaEspecial(String id, String conteudo) {
        return pagina(PAGINA_ESPECIAL, id, conteudo);
    }

    public RepositorioCartasBuilder paginaEspecial(String id, Pagina conteudo) {
        return pagina(PAGINA_ESPECIAL, id, conteudo);
    }

    public boolean buildSemGit() {
        return criarEstruturaRepositorioCartas()
                && criarPaginas();
    }

    public boolean build() {
        return buildSemGit() && commitPush();
    }

    private boolean commitPush() {
        try {
            Git git = Git.open(this.localRepositorio.toFile());
            git.add().addFilepattern(".").call();
            git.commit()
                    .setAuthor("Teste", "teste.automatizado@gmail.com")
                    .setMessage("setup de testes")
                    .call();
            git.push().setPushAll().call();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean criarPaginas() {
        EscritorDeArquivos escritor = new EscritorDeArquivos();

        this.paginas.entrySet()
                .stream()
                .forEach(entry -> escritor.escrever(this.localRepositorio.resolve(entry.getKey()), entry.getValue()));

        return true;
    }

    private RepositorioCartasBuilder pagina(TipoPagina tipo, String id, Pagina conteudo) {
        return pagina(tipo, id, formatadorPagina.formatar(conteudo.withTipo(tipo.getNome())));
    }

    private RepositorioCartasBuilder pagina(TipoPagina tipo, String id, String conteudo) {
        Path p = Paths.get(tipo.getCaminhoPasta().toString(), id + "." + tipo.getExtensao());
        paginas.put(p, conteudo);

        return this;
    }

    private boolean criarEstruturaRepositorioCartas() {
        return asList(values())
                .stream()
                .map(t -> localRepositorio.resolve(t.getCaminhoPasta()))
                .map(Path::toFile)
                .map(f -> {
                    f.mkdirs();
                    return uncheckedSupplier(() -> f.toPath().resolve("dummy").toFile().createNewFile());
                })
                .allMatch(x -> x.get());
    }

}
