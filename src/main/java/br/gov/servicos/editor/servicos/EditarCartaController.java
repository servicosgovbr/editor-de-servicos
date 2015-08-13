package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.cartas.Carta;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

        response.setHeader("X-Git-Revision", metadados.getRevisao());
        response.setHeader("X-Git-Author", metadados.getAutor());
        response.setDateHeader("Last-Modified", metadados.getHorario().getTime());

        return carta.getConteudo();
    }

    @ResponseBody
    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void naoEncontrado() {
    }

}
