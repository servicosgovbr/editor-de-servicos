package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.MetadadosUtils;
import br.gov.servicos.editor.conteudo.ConteudoVersionadoFactory;
import br.gov.servicos.editor.security.UserProfiles;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

import static br.gov.servicos.editor.conteudo.TipoPagina.SERVICO;
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

    @RequestMapping(value = "/editar/api/pagina/servico/{id}/{novoNome}", method = PATCH)
    ResponseEntity renomear(@PathVariable("id") String id,
                            @PathVariable String novoNome,
                            HttpServletResponse response) {
        ConteudoVersionado carta = renomear(id, novoNome);

        return new ResponseEntity(MetadadosUtils.metadados(carta), HttpStatus.OK);
    }

    ConteudoVersionado renomear(String id, String novoNome) {
        ConteudoVersionado carta = factory.pagina(id, SERVICO);
        String novoId = carta.renomear(userProfiles.get(), novoNome);
        return factory.pagina(novoId, SERVICO);
    }

}
