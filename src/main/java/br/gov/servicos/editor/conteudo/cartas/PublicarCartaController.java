package br.gov.servicos.editor.conteudo.cartas;

import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class PublicarCartaController {

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/editar/api/servico/{id}", method = PUT)
    void publicar(@PathVariable("id") Carta carta) {
        carta.publicar();
    }

}
