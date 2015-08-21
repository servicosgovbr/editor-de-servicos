package br.gov.servicos.editor.frontend;

import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class Orgaos {

    public static final String URL = "http://estruturaorganizacional.dados.gov.br/doc/orgao-entidade/resumida.json";

    RestTemplate rest;

    @Autowired
    public Orgaos(RestTemplate rest) {
        this.rest = rest;
    }

    @Cacheable("orgaos")
    public String get() {
        log.info("Requisitando lista de órgãos no Siorg...");
        try {
            return rest.getForEntity(URL, String.class).getBody();
        } catch(Exception e) {
            log.error("Erro ao requisitar lista de órgãos do Siorg", e);
            throw e;
        }
    }
}
