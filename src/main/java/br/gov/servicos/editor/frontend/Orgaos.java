package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.servicos.Orgao;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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
    public List<Orgao> get(final String orgao) {
        log.info("Requisitando lista de órgãos no Siorg...");
        try {
            //return rest.getForEntity(URL, String.class).getBody();
            List<Orgao> list = new ArrayList<>();
            list.add(new Orgao("abc", "abc"));
            list.add(new Orgao("abc2", "abc2"));
            return list;
        } catch(Exception e) {
            log.error("Erro ao requisitar lista de órgãos do Siorg", e);
            throw e;
        }
    }
}
