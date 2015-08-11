package br.gov.servicos.editor.servicos;

import com.github.slugify.Slugify;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class ServicoController {

    Cartas cartas;
    Slugify slugify;

    @Autowired
    ServicoController(Cartas cartas, Slugify slugify) {
        this.cartas = cartas;
        this.slugify = slugify;
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/servicos", method = GET)
    Iterable<Metadados> listar() throws IOException {
        return cartas.listar();
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/servico/v3/{id}", method = GET, produces = "application/xml")
    String editarV3(
            @PathVariable("id") String id,
            HttpServletResponse response
    ) throws IOException {
        return carregarServico(id, response, cartas::ultimaRevisaoV3, cartas::conteudoServicoV3);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/excluir/api/servico/{id}", method = DELETE)
    void excluirServico(
            @PathVariable("id") String id,
            @AuthenticationPrincipal User usuario
    ) throws IOException {
        cartas.excluir(id, usuario);

    }

    private String carregarServico(
            @PathVariable("id") String unsafeId,
            HttpServletResponse response,
            Function<String, Optional<Metadados>> ultimaRevisao,
            Function<String, Optional<String>> carregador
    ) throws FileNotFoundException {
        String id = this.slugify.slugify(unsafeId);

        Optional<Metadados> m = ultimaRevisao.apply(id);

        m.ifPresent(metadados -> {
            response.setHeader("X-Git-Revision", metadados.getRevisao());
            response.setHeader("X-Git-Author", metadados.getAutor());
            response.setDateHeader("Last-Modified", metadados.getHorario().getTime());
        });

        return carregador.apply(id)
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
