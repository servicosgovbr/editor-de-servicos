package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.MetadadosUtils;
import br.gov.servicos.editor.conteudo.paginas.*;
import br.gov.servicos.editor.git.Metadados;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Optional;

import static br.gov.servicos.editor.conteudo.paginas.TipoPagina.SERVICO;
import static java.lang.String.join;
import static java.lang.String.valueOf;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EditarCartaController {

    private ConteudoVersionadoFactory factory;

    @Autowired
    public EditarCartaController(ConteudoVersionadoFactory factory) {
        this.factory = factory;
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/pagina/servico/{id}", method = GET, produces = "application/xml")
    ResponseEntity editar(
            @PathVariable("id") String id) throws ConteudoInexistenteException, FileNotFoundException {
        ConteudoVersionado carta = factory.pagina(id, SERVICO);

        if (!carta.existe()) {
            throw new ConteudoInexistenteException(carta);
        }

        return new ResponseEntity(carta.getConteudoRaw(), MetadadosUtils.metadados(carta), OK);
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/pagina/servico/novo", method = GET, produces = "application/xml")
    String editarNovo() {
        return "<servico/>";
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/pagina/{tipo}/{id}", method = GET, produces = "application/json")
    public String editar(
            @PathVariable("tipo") String tipo,
            @PathVariable("id") String id,
            HttpServletResponse response) throws FileNotFoundException, ConteudoInexistenteException {
        return editar((PaginaVersionada) factory.pagina(id, TipoPagina.fromNome(tipo)), response);
    }


    @ResponseBody
    @RequestMapping(value = "/editar/api/pagina/{tipo}/novo", method = GET, produces = "application/json")
    public Pagina editarNovo(@PathVariable("tipo") String tipo) {
        return new Pagina().withTipo(TipoPagina.fromNome(tipo).getNome());
    }

    private String editar(PaginaVersionada paginaVersionada, HttpServletResponse response) throws FileNotFoundException, ConteudoInexistenteException {
        if (!paginaVersionada.existe()) {
            throw new ConteudoInexistenteException(paginaVersionada);
        }

        Metadados<Pagina> metadados = paginaVersionada.getMetadados();

        Optional.ofNullable(metadados.getPublicado()).ifPresent(r -> {
            response.setHeader("X-Git-Commit-Publicado", r.getHash());
            response.setHeader("X-Git-Autor-Publicado", r.getAutor());
            response.setHeader("X-Git-Horario-Publicado", valueOf(r.getHorario().getTime()));
        });

        Optional.ofNullable(metadados.getEditado()).ifPresent(r -> {
            response.setHeader("X-Git-Commit-Editado", r.getHash());
            response.setHeader("X-Git-Autor-Editado", r.getAutor());
            response.setHeader("X-Git-Horario-Editado", valueOf(r.getHorario().getTime()));
        });

        return converterParaJson(conteudoCompleto(paginaVersionada));
    }

    private Pagina conteudoCompleto(PaginaVersionada paginaVersionada) throws FileNotFoundException {
        String[] linhas = paginaVersionada.getConteudoRaw().split("\n");

        int posicaoCabecalhoConteudo = linhas.length > 2 ? 3 : linhas.length;

        return new Pagina()
                .withTipo(paginaVersionada.getTipo().getNome())
                .withNome(linhas[0])
                .withConteudo(join("\n", Arrays.copyOfRange(linhas, posicaoCabecalhoConteudo, linhas.length)));
    }

    @SneakyThrows
    private String converterParaJson(Pagina pagina) throws FileNotFoundException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(pagina);
    }
}
