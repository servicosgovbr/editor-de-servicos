package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.MetadadosUtils;
import br.gov.servicos.editor.conteudo.paginas.ConteudoVersionadoFactory;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;

import static br.gov.servicos.editor.conteudo.paginas.TipoPagina.SERVICO;
import static java.lang.String.valueOf;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class EditarCartaController {

    private ConteudoVersionadoFactory factory;

    @Autowired
    public EditarCartaController(ConteudoVersionadoFactory factory) {
        this.factory = factory;
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/pagina/servico/{id}", method = GET, produces = "application/xml")
    String editar(
            @PathVariable("id") String id,
            HttpServletResponse response) throws ConteudoInexistenteException, FileNotFoundException {
        ConteudoVersionado carta = factory.pagina(id, SERVICO);

        if (!carta.existe()) {
            throw new ConteudoInexistenteException(carta);
        }

        MetadadosUtils.metadados(carta)
                .entrySet()
                .forEach(e -> response.setHeader(e.getKey(), e.getValue()));

        return carta.getConteudoRaw();
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/pagina/servico/novo", method = GET, produces = "application/xml")
    String editarNovo() {
        return "<servico/>";
    }

}
