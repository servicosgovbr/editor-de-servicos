package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.security.UserProfiles;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class RenomearCartaController {

    UserProfiles userProfiles;

    @Autowired
    public RenomearCartaController(UserProfiles userProfiles) {
        this.userProfiles = userProfiles;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/editar/api/servico/{id}/{novoNome}", method = PATCH)
    void renomear(@PathVariable("id") Carta carta, @PathVariable String novoNome) {
        carta.renomear(userProfiles.get(), novoNome);
    }

}
