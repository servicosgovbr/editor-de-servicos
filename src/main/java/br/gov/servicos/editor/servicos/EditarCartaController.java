package br.gov.servicos.editor.servicos;

import com.github.slugify.Slugify;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class EditarCartaController {

    Cartas cartas;
    Slugify slugify;

    @Autowired
    EditarCartaController(Cartas cartas, Slugify slugify) {
        this.cartas = cartas;
        this.slugify = slugify;
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/servico/v3/{id}", method = GET, produces = "application/xml")
    String editarV3(
            @PathVariable("id") String unsafeId,
            HttpServletResponse response
    ) throws IOException {
        String id = this.slugify.slugify(unsafeId);

        Optional<Metadados> m = cartas.ultimaRevisao(id);

        m.ifPresent(metadados -> {
            response.setHeader("X-Git-Revision", metadados.getRevisao());
            response.setHeader("X-Git-Author", metadados.getAutor());
            response.setDateHeader("Last-Modified", metadados.getHorario().getTime());
        });

        return cartas.conteudoServico(id)
                .orElseThrow(() -> new FileNotFoundException(
                        "Não foi possível encontrar o serviço referente ao arquivo '" + unsafeId + "'"
                ));
    }

    @ResponseBody
    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void naoEncontrado() {
    }

}
