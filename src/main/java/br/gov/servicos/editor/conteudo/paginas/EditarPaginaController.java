package br.gov.servicos.editor.conteudo.paginas;


import br.gov.servicos.editor.git.Metadados;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.util.Arrays;

import static java.lang.String.*;
import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EditarPaginaController {

    private PaginaVersionadaFactory factory;

    @Autowired
    public EditarPaginaController(PaginaVersionadaFactory factory) {
        this.factory = factory;
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/pagina/{tipo}/{id}", method = GET, produces = "application/json")
    public String editar(
            @PathVariable("tipo") String tipo,
            @PathVariable("id") String id,
            HttpServletResponse response) throws FileNotFoundException {
        return editar(factory.pagina(id, TipoPagina.fromNome(tipo)), response);
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/pagina/{tipo}/novo", method = GET, produces = "application/json")
    Pagina editarNovo(@PathVariable("tipo") String tipo) {
        return new Pagina().withTipo(TipoPagina.fromNome(tipo).getNome());
    }

    private String editar(PaginaVersionada paginaVersionada, HttpServletResponse response) throws FileNotFoundException {
        Metadados<Pagina> metadados = paginaVersionada.getMetadados();

        ofNullable(metadados.getPublicado()).ifPresent(r -> {
            response.setHeader("X-Git-Commit-Publicado", r.getHash());
            response.setHeader("X-Git-Autor-Publicado", r.getAutor());
            response.setHeader("X-Git-Horario-Publicado", valueOf(r.getHorario().getTime()));
        });

        ofNullable(metadados.getEditado()).ifPresent(r -> {
            response.setHeader("X-Git-Commit-Editado", r.getHash());
            response.setHeader("X-Git-Autor-Editado", r.getAutor());
            response.setHeader("X-Git-Horario-Editado", valueOf(r.getHorario().getTime()));
        });

        Pagina pagina = carregarConteudoPagina(paginaVersionada);

        return converterParaJson(pagina);
    }

    @SneakyThrows
    private String converterParaJson(Pagina pagina) throws FileNotFoundException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(pagina);
    }

    private Pagina carregarConteudoPagina(PaginaVersionada paginaVersionada) throws FileNotFoundException {
        String[] linhas = paginaVersionada.getConteudoRaw().split("\n");
        int posicaoCabecalhoConteudo = linhas.length > 2 ? 3 : linhas.length;

        Pagina pagina = paginaVersionada
                .getMetadados()
                .getConteudo()
                .withTipo(paginaVersionada.getTipo().getNome());

        pagina.setConteudo(join("\n", Arrays.copyOfRange(linhas, posicaoCabecalhoConteudo, linhas.length)));

        return pagina;
    }
}
