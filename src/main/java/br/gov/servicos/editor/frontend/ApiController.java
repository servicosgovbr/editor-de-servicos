package br.gov.servicos.editor.frontend;

import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import static lombok.AccessLevel.PRIVATE;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequestMapping("/editar/api")
class ApiController {

    @Cacheable("orgaos")
    @RequestMapping("/orgaos")
    @ResponseBody
    String orgaos() {
        ResponseEntity<String> entity = new RestTemplate()
                .getForEntity("http://estruturaorganizacional.dados.gov.br/doc/orgao-entidade/resumida.json", String.class);

        return entity.getBody();
    }

}
