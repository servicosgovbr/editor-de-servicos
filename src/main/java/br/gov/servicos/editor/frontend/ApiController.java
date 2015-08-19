package br.gov.servicos.editor.frontend;

import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

import static lombok.AccessLevel.PRIVATE;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequestMapping("/editar/api")
class ApiController {

    VCGE vcge;
    Orgaos orgaos;

    @Autowired
    public ApiController(VCGE vcge, Orgaos orgaos) {
        this.vcge = vcge;
        this.orgaos = orgaos;
    }

    @RequestMapping("/ping")
    @ResponseBody
    Ping ping(@AuthenticationPrincipal User user) {
        return new Ping(user.getUsername(), new Date().getTime());
    }

    @RequestMapping("/vcge")
    @ResponseBody
    String vcge() {
        return vcge.get();
    }

    @RequestMapping("/orgaos")
    @ResponseBody
    String orgaos() {
        return orgaos.get();
    }

    @Data
    @FieldDefaults(level = PRIVATE, makeFinal = true)
    static class Ping {
        String login;
        Long horario;
    }

}
