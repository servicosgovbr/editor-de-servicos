package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.ConteudoVersionadoFactory;
import br.gov.servicos.editor.conteudo.MetadadosUtils;
import br.gov.servicos.editor.security.UserProfiles;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @RequestMapping(value = "/editar/api/pagina/servico/{id}", method = PATCH)
    ResponseEntity renomear(@PathVariable("id") String id,
                            @RequestBody String novoNome) {
        ConteudoVersionado carta = factory.pagina(id, SERVICO);

        String orgaoId = carta.getOrgaoId();
        if (!userProfiles.temPermissaoParaOrgao(SERVICO, orgaoId)) {
            throw new AccessDeniedException("Usuário sem permissão");
        }

        String novoId = carta.renomear(userProfiles.get(), novoNome);
        carta = factory.pagina(novoId, SERVICO);

        return new ResponseEntity(MetadadosUtils.metadados(carta), HttpStatus.OK);
    }
}
