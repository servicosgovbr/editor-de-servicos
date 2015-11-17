package br.gov.servicos.editor.conteudo;

import br.gov.servicos.editor.conteudo.cartas.ConteudoInexistenteException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.util.Arrays;

import static br.gov.servicos.editor.conteudo.TipoPagina.SERVICO;
import static java.lang.String.join;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EditarPaginaController {

    private ConteudoVersionadoFactory factory;

    @Autowired
    public EditarPaginaController(ConteudoVersionadoFactory factory) {
        this.factory = factory;
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/pagina/servico/{id}", method = GET, produces = APPLICATION_XML_VALUE)
    public ResponseEntity editar(@PathVariable("id") String id) throws ConteudoInexistenteException, FileNotFoundException {
        ConteudoVersionado carta = factory.pagina(id, SERVICO);
        if (!carta.existe()) {
            throw new ConteudoInexistenteException(carta);
        }
        return new ResponseEntity(carta.getConteudoRaw(), MetadadosUtils.metadados(carta), OK);
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/pagina/servico/novo", method = GET, produces = APPLICATION_XML_VALUE)
    public String editarNovo() {
        return "<servico/>";
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/pagina/{tipo}/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity editar(
            @PathVariable("tipo") String tipo,
            @PathVariable("id") String id) throws FileNotFoundException, ConteudoInexistenteException {
        return editar((PaginaVersionada) factory.pagina(id, TipoPagina.fromNome(tipo)));
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/pagina/{tipo}/novo", method = GET, produces = APPLICATION_JSON_VALUE)
    public Pagina editarNovo(@PathVariable("tipo") String tipo) {
        return new Pagina().withTipo(TipoPagina.fromNome(tipo).getNome());
    }

    private ResponseEntity editar(PaginaVersionada paginaVersionada) throws FileNotFoundException, ConteudoInexistenteException {
        if (!paginaVersionada.existe()) {
            throw new ConteudoInexistenteException(paginaVersionada);
        }

        HttpHeaders headers = MetadadosUtils.metadados(paginaVersionada);
        String conteudoRetorno = converterParaJson(conteudoCompleto(paginaVersionada));

        return new ResponseEntity(conteudoRetorno, headers, OK);
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
