package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.MetadadosUtils;
import br.gov.servicos.editor.conteudo.paginas.ConteudoVersionadoFactory;
import br.gov.servicos.editor.security.UserProfiles;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static br.gov.servicos.editor.conteudo.paginas.TipoPagina.SERVICO;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class DespublicarCartaController {

    private ConteudoVersionadoFactory factory;
    UserProfiles userProfiles;

    @Autowired
    public DespublicarCartaController(ConteudoVersionadoFactory factory, UserProfiles userProfiles) {
        this.factory = factory;
        this.userProfiles = userProfiles;
    }

    @RequestMapping(value = "/editar/api/pagina/servico/{id}/despublicar", method = POST)
    ResponseEntity despublicar(@PathVariable("id") String id) throws ConteudoInexistenteException {
        ConteudoVersionado carta = factory.pagina(id, SERVICO);
        if (!carta.existe()) {
            throw new ConteudoInexistenteException(carta);
        }
        carta.despublicarAlteracoes(userProfiles.get());

        return new ResponseEntity(MetadadosUtils.metadados(carta), HttpStatus.OK);
    }

}
