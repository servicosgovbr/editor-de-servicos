package br.gov.servicos.editor.conteudo;

import br.gov.servicos.editor.conteudo.cartas.ConteudoInexistenteException;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;

import static br.gov.servicos.editor.conteudo.TipoPagina.fromNome;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EditarPaginaController {

    ConteudoVersionadoFactory factory;

    @Autowired
    public EditarPaginaController(ConteudoVersionadoFactory factory) {
        this.factory = factory;
    }

    @RequestMapping(value = "/editar/api/pagina/{tipo}/{id}", method = GET, produces = APPLICATION_XML_VALUE)
    public ResponseEntity<String> editar(@PathVariable("tipo") String tipo, @PathVariable("id") String id) throws ConteudoInexistenteException, FileNotFoundException {
        ConteudoVersionado carta = factory.pagina(id, fromNome(tipo));
        if (!carta.existe()) {
            throw new ConteudoInexistenteException(carta);
        }
        return new ResponseEntity<>(carta.getConteudoRaw(), MetadadosUtils.metadados(carta), OK);
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/pagina/{tipo}/novo", method = GET, produces = APPLICATION_XML_VALUE)
    public String editarNovo(@PathVariable String tipo) {
        return "<" + fromNome(tipo) + "/>";
    }

}
