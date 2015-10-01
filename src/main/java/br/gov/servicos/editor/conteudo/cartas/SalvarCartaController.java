package br.gov.servicos.editor.conteudo.cartas;

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

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SalvarCartaController {

    ReformatadorXml reformatadorXml;
    UserProfiles userProfiles;

    @Autowired
    public SalvarCartaController(ReformatadorXml reformatadorXml, UserProfiles userProfiles) {
        this.reformatadorXml = reformatadorXml;
        this.userProfiles = userProfiles;
    }

    @RequestMapping(value = "/editar/v3/servico/{id}", method = POST)
    RedirectView salvar(
            @PathVariable("id") Carta carta,
            @RequestBody DOMSource servico
    ) throws Exception {
        String conteudo = reformatadorXml.formata(servico);

        carta.salvar(userProfiles.get(), conteudo);

        return new RedirectView("/editar/api/servico/v3/" + carta.getId());
    }

}
