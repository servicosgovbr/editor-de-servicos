package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.paginas.ConteudoVersionadoFactory;
import br.gov.servicos.editor.security.UserProfiles;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.view.RedirectView;

import static br.gov.servicos.editor.conteudo.paginas.TipoPagina.SERVICO;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class DescartarAlteracoesCartaController {

    private ConteudoVersionadoFactory factory;
    UserProfiles userProfiles;

    @Autowired
    public DescartarAlteracoesCartaController(ConteudoVersionadoFactory factory, UserProfiles userProfiles) {
        this.factory = factory;
        this.userProfiles = userProfiles;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/editar/api/pagina/servico/{id}/descartar", method = POST)
    RedirectView descartar(@PathVariable("id") String id) throws ConteudoInexistenteException {
        ConteudoVersionado carta = factory.pagina(id, SERVICO);

        if (!carta.existe()) {
            throw new ConteudoInexistenteException(carta);
        }
        if (!carta.existeNoMaster()) {
            throw new IllegalStateException("Descartar um serviço que não foi publicado, seria o equivalente a excluir o serviço");
        }

        carta.descartarAlteracoes();

        return new RedirectView("/editar/api/pagina/servico/" + carta.getId());
    }

}
