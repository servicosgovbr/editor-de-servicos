package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.paginas.ConteudoVersionadoFactory;
import br.gov.servicos.editor.security.UserProfiles;
import br.gov.servicos.editor.utils.ReformatadorXml;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.xml.transform.dom.DOMSource;

import static br.gov.servicos.editor.conteudo.paginas.TipoPagina.SERVICO;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SalvarCartaController {

    ReformatadorXml reformatadorXml;
    UserProfiles userProfiles;
    ConteudoVersionadoFactory factory;

    @Autowired
    public SalvarCartaController(ReformatadorXml reformatadorXml, UserProfiles userProfiles, ConteudoVersionadoFactory factory) {
        this.reformatadorXml = reformatadorXml;
        this.userProfiles = userProfiles;
        this.factory = factory;
    }

    @RequestMapping(value = "/editar/api/pagina/servico/{id}", method = POST)
    RedirectView salvar(
            @PathVariable("id") String id,
            @RequestBody DOMSource servico) throws Exception {
        ConteudoVersionado carta = factory.pagina(id, SERVICO);

        String conteudo = reformatadorXml.formata(servico);
        carta.salvar(userProfiles.get(), conteudo);

        return new RedirectView("/editar/api/pagina/servico/" + carta.getId());
    }

}
