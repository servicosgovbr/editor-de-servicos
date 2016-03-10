package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.ConteudoVersionadoFactory;
import br.gov.servicos.editor.conteudo.TipoPagina;
import br.gov.servicos.editor.security.CheckOrgaoEspecificoController;
import br.gov.servicos.editor.security.TipoPermissao;
import br.gov.servicos.editor.security.UserProfiles;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.view.RedirectView;

import static br.gov.servicos.editor.conteudo.TipoPagina.fromNome;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class DescartarAlteracoesPaginaController extends CheckOrgaoEspecificoController {

    ConteudoVersionadoFactory factory;
    UserProfiles userProfiles;

    @Autowired
    public DescartarAlteracoesPaginaController(ConteudoVersionadoFactory factory, UserProfiles userProfiles) {
        this.factory = factory;
        this.userProfiles = userProfiles;
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/editar/api/pagina/{tipo}/{id}/descartar", method = POST)
    RedirectView descartar(@PathVariable("tipo") String tipo, @PathVariable("id") String id) throws ConteudoInexistenteException {
        TipoPagina tipoPagina = fromNome(tipo);
        ConteudoVersionado conteudoVersionado = factory.pagina(id, tipoPagina);

        if (!conteudoVersionado.existe()) {
            throw new ConteudoInexistenteException(conteudoVersionado);
        }

        if (!conteudoVersionado.existeNoMaster()) {
            throw new IllegalStateException("Descartar um serviço que não foi publicado é equivalente a excluir o serviço");
        }

        if (!usuarioPodeRealizarAcao(userProfiles, tipoPagina, conteudoVersionado.getOrgaoId())) {
            throw new AccessDeniedException("Usuário sem permissão");
        }

        conteudoVersionado.descartarAlteracoes();
        return new RedirectView("/editar/api/pagina/" + tipo + '/' + conteudoVersionado.getId(), true, false);
    }

    @Override
    public TipoPermissao getTipoPermissao() {
        return TipoPermissao.DESCARTAR;
    }

}
