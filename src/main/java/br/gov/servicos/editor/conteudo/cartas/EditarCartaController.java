package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.paginas.ConteudoVersionadoFactory;
import br.gov.servicos.editor.git.Metadados;
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
import static java.util.Optional.ofNullable;
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

        Metadados<Carta.Servico> metadados = carta.getMetadados();

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

        return carta.getConteudoRaw();
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/pagina/servico/novo", method = GET, produces = "application/xml")
    String editarNovo() {
        return "<servico/>";
    }

}
