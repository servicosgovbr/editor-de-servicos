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
    private PaginaVersionadaFactory factory;
    UserProfiles userProfiles;

    @Autowired
    public SalvarPaginaController(FormatadorConteudoPagina formatador, PaginaVersionadaFactory factory, UserProfiles userProfiles) {
        this.formatador = formatador;
        this.factory = factory;
        this.userProfiles = userProfiles;
    }

    @ResponseBody
    @RequestMapping(value = "/editar/pagina/{tipo}/{id}", method = POST)
    RedirectView editar(@PathVariable("tipo") String tipo,
                        @PathVariable("id") String id,
                        @RequestBody Pagina pagina) {
        PaginaVersionada paginaVersionada = factory.pagina(id, TipoPagina.fromNome(tipo));
        paginaVersionada.salvar(userProfiles.get(), formatador.formatar(pagina));
        return new RedirectView("/editar/api/pagina/" + tipo + "/" + paginaVersionada.getId());
    }

}
