package br.gov.servicos.editor.frontend;

import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequestMapping("/editar/api")
class ApiController {

    @RequestMapping("/ping")
    @ResponseBody
    Ping ping(@AuthenticationPrincipal User user) {
        return new Ping(user.getUsername(), new Date().getTime());
    }

    @Cacheable("orgaos")
    @RequestMapping("/orgaos")
    @ResponseBody
    String orgaos() {
        ResponseEntity<String> entity = new RestTemplate()
                .getForEntity("http://estruturaorganizacional.dados.gov.br/doc/orgao-entidade/resumida.json", String.class);

        return entity.getBody();
    }

    @Data
    @FieldDefaults(level = PRIVATE, makeFinal = true)
    static class Ping {
        String login;
        Long horario;
    }
}
