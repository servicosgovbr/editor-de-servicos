package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.config.ConteudoDeReferencia;
import br.gov.servicos.editor.servicos.Orgao;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

import static lombok.AccessLevel.PRIVATE;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequestMapping("/editar/api")
class ApiController {

    @RequestMapping("/usuario")
    @ResponseBody
    User usuario(@AuthenticationPrincipal User usuario) {
        return usuario;
    }

    @RequestMapping("/areas-de-interesse")
    @ResponseBody
    Collection<String> areasDeInteresse(ConteudoDeReferencia ref) {
        return ref.areasDeInteresse();
    }

    @RequestMapping("/eventos-da-linha-da-vida")
    @ResponseBody
    Collection<String> eventosDaLinhaDaVida(ConteudoDeReferencia ref) {
        return ref.eventosDaLinhaDaVida();
    }

    @RequestMapping("/orgaos")
    @ResponseBody
    Collection<Orgao> orgaos(ConteudoDeReferencia ref) {
        return ref.orgaos();
    }

    @RequestMapping("/segmentos-da-sociedade")
    @ResponseBody
    Collection<String> segmentosDaSociedade(ConteudoDeReferencia ref) {
        return ref.segmentosDaSociedade();
    }

    @RequestMapping("/tipos-de-canais-de-prestacao")
    @ResponseBody
    Collection<String> tiposDeCanalDePrestacao(ConteudoDeReferencia ref) {
        return ref.tiposDeCanalDePrestacao();
    }

}
