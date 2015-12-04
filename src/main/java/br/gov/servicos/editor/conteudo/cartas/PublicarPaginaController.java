package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.ConteudoVersionadoFactory;
import br.gov.servicos.editor.conteudo.MetadadosUtils;
import br.gov.servicos.editor.conteudo.TipoPagina;
import br.gov.servicos.editor.security.UserProfiles;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static br.gov.servicos.editor.conteudo.TipoPagina.fromNome;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class PublicarPaginaController {

    private ConteudoVersionadoFactory factory;
    UserProfiles userProfiles;

    @Autowired
    public PublicarPaginaController(ConteudoVersionadoFactory factory, UserProfiles userProfiles) {
        this.factory = factory;
        this.userProfiles = userProfiles;
    }

    @RequestMapping(value = "/editar/api/pagina/{tipo}/{id}", method = PUT)
    ResponseEntity publicar(@PathVariable("tipo") String tipo, @PathVariable("id") String id) throws ConteudoInexistenteException {
        TipoPagina tipoPagina = fromNome(tipo);
        ConteudoVersionado conteudoVersionado = factory.pagina(id, tipoPagina);

        String orgaoId = conteudoVersionado.getOrgaoId();
        if (!userProfiles.temPermissaoParaOrgao(tipoPagina, orgaoId)) {
            throw new AccessDeniedException("Usuário sem permissão");
        }

        if (!conteudoVersionado.existe()) {
            throw new ConteudoInexistenteException(conteudoVersionado);
        }

        conteudoVersionado.publicar(userProfiles.get());

        return new ResponseEntity(MetadadosUtils.metadados(conteudoVersionado), HttpStatus.OK);
    }

}
