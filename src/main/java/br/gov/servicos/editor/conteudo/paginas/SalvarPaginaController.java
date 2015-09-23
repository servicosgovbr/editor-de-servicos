package br.gov.servicos.editor.conteudo.paginas;


import br.gov.servicos.editor.oauth2.UserProfiles;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SalvarPaginaController {

    FormatadorConteudoPagina formatador;
    UserProfiles userProfiles;

    @Autowired
    public SalvarPaginaController(FormatadorConteudoPagina formatador, UserProfiles userProfiles) {
        this.formatador = formatador;
        this.userProfiles = userProfiles;
    }

    @ResponseBody
    @RequestMapping(value = "/editar/orgao/{id}", method = POST)
    RedirectView editar(@PathVariable("id") PaginaVersionada pagina,
                        @RequestBody Pagina orgao) {

        pagina.salvar(userProfiles.get(), formatador.formatar(orgao));

        return new RedirectView("/editar/api/orgao/" + pagina.getId());
    }

}
