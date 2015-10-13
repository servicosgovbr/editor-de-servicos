package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.paginas.ConteudoVersionadoFactory;
import br.gov.servicos.editor.security.UserProfiles;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static br.gov.servicos.editor.conteudo.paginas.TipoPagina.SERVICO;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class RenomearCartaController {

    UserProfiles userProfiles;
    private ConteudoVersionadoFactory factory;

    @Autowired
    public RenomearCartaController(UserProfiles userProfiles, ConteudoVersionadoFactory factory) {
        this.userProfiles = userProfiles;
        this.factory = factory;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/editar/api/pagina/servico/{id}/{novoNome}", method = PATCH)
    void renomear(@PathVariable("id") String id, @PathVariable String novoNome) {
        ConteudoVersionado carta = factory.pagina(id, SERVICO);
        carta.renomear(userProfiles.get(), novoNome);
    }

}
