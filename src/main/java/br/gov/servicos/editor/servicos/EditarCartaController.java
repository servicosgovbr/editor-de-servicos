package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.cartas.Carta;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class EditarCartaController {

    @ResponseBody
    @RequestMapping(value = "/editar/api/servico/v3/{id}", method = GET, produces = "application/xml")
    String editar(
            @PathVariable("id") Carta carta,
            HttpServletResponse response
    ) throws FileNotFoundException {
        Metadados metadados = carta.getMetadados();

        metadados.getPublicado().ifPresent(r -> {
            response.setHeader("X-Git-Commit-Publicado", r.getHash());
            response.setHeader("X-Git-Autor-Publicado", r.getAutor());
            response.setDateHeader("X-Git-Horario-Publicado", r.getHorario().getTime());
        });

        metadados.getEditado().ifPresent(r -> {
            response.setHeader("X-Git-Commit-Editado", r.getHash());
            response.setHeader("X-Git-Autor-Editado", r.getAutor());
            response.setDateHeader("X-Git-Horario-Editado", r.getHorario().getTime());
        });

        return carta.getConteudo();
    }

}
