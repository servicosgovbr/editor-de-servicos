package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.cartas.Carta;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class PublicarCartaController {

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/editar/api/servico/{id}", method = PUT)
    void publicar(
            @PathVariable("id") Carta carta,
            @AuthenticationPrincipal User usuario
    ) {
        carta.publicar(usuario);
    }

}
