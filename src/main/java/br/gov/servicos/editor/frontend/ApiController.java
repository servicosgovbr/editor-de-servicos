package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.conteudo.ListaDeConteudo;
import br.gov.servicos.editor.oauth2.UserProfiles;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequestMapping("/editar/api")
class ApiController {

    Orgaos orgaos;
    Siorg siorg;
    VCGE vcge;
    UserProfiles profiles;
    ListaDeConteudo listaDeConteudo;

    @Autowired
    public ApiController(Orgaos orgaos, Siorg siorg, VCGE vcge, UserProfiles profiles, ListaDeConteudo listaDeConteudo) {
        this.orgaos = orgaos;
        this.siorg = siorg;
        this.vcge = vcge;
        this.profiles = profiles;
        this.listaDeConteudo = listaDeConteudo;
    }

    @RequestMapping("/ping")
    @ResponseBody
    Ping ping() {
        return new Ping(profiles.get(), new Date().getTime());
    }

    @RequestMapping("/vcge")
    @ResponseBody
    String vcge() {
        return vcge.get();
    }

    @RequestMapping("/orgaos")
    @ResponseBody
    List<Orgao> orgaos(@RequestParam("q") String termo) {
        return orgaos.get(termo);
    }

    @RequestMapping("/orgao")
    @ResponseBody
    String orgao(@RequestParam("urlOrgao") String urlOrgao) throws IOException {
        return siorg.nomeDoOrgao(urlOrgao).orElse("");
    }

    @RequestMapping("/id-unico/{id}")
    @ResponseBody
    boolean isIdUnico(@PathVariable("id") String id) {
        return listaDeConteudo.isIdUnico(id);
    }

}
