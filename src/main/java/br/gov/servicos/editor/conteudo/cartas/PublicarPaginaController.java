package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.ConteudoVersionadoFactory;
import br.gov.servicos.editor.conteudo.MetadadosUtils;
import br.gov.servicos.editor.conteudo.TipoPagina;
import br.gov.servicos.editor.security.CheckOrgaoEspecificoController;
import br.gov.servicos.editor.security.TipoPermissao;
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
class PublicarPaginaController extends CheckOrgaoEspecificoController {

    ConteudoVersionadoFactory factory;

    UserProfiles userProfiles;

    @Autowired
    public PublicarPaginaController(ConteudoVersionadoFactory factory, UserProfiles userProfiles) {
        this.factory = factory;
        this.userProfiles = userProfiles;
    }

    @RequestMapping(value = "/editar/api/pagina/{tipo}/{id}", method = PUT)
    ResponseEntity<Void> publicar(@PathVariable("tipo") String tipo, @PathVariable("id") String id) {
        TipoPagina tipoPagina = fromNome(tipo);
        ConteudoVersionado conteudoVersionado = factory.pagina(id, tipoPagina);

        if (!conteudoVersionado.existe()) {
            throw new ConteudoInexistenteException(conteudoVersionado);
        }

        if (!usuarioPodeRealizarAcao(userProfiles, tipoPagina, conteudoVersionado.getOrgaoId())) {
            throw new AccessDeniedException("Usuário sem permissão");
        }

        conteudoVersionado.publicar(userProfiles.get());

        return new ResponseEntity<>(MetadadosUtils.metadados(conteudoVersionado), HttpStatus.OK);
    }

    @Override
    public TipoPermissao getTipoPermissao() {
        return TipoPermissao.PUBLICAR;
    }
}
