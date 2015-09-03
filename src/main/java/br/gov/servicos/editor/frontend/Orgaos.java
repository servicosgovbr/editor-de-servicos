package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.servicos.Orgao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.github.slugify.Slugify;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.zip.GZIPInputStream;

import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class Orgaos implements InitializingBean {

    public static final ClassPathResource RESOURCE = new ClassPathResource("estrutura-organizacional.json.gz");

    RestTemplate rest;
    ObjectReader reader;
    Slugify slugify;

    @NonFinal
    EstruturaOrganizacional estruturaOrganizacional;

    @Autowired
    public Orgaos(RestTemplate rest, ObjectMapper mapper, Slugify slugify) {
        this.rest = rest;
        this.slugify = slugify;
        this.reader = mapper.reader(EstruturaOrganizacional.class);
    }

    @Override
    @SneakyThrows
    public void afterPropertiesSet() throws Exception {
        log.debug("Lendo estrutura organizacional de {}...", RESOURCE);
        estruturaOrganizacional = reader.readValue(new GZIPInputStream(RESOURCE.getInputStream()));
        log.debug("Estrutura organizacional lida com sucesso", RESOURCE);
    }

    @Cacheable("orgaos")
    public List<Orgao> get(String termo) {
        log.info("Buscando órgãos com termo '{}'", termo);
        return estruturaOrganizacional.getUnidades()
                .stream()
                .filter(u -> slugify.slugify(u.getNome()).replace('-', ' ').contains(slugify.slugify(termo).replace('-', ' '))
                        || slugify.slugify(u.getNome()).replace('-', ' ').contains(slugify.slugify(termo).replace('-', ' ')))
                        .map(u -> new Orgao().withNome(u.getNome()).withId(u.getCodigoUnidade()))
                        .sorted((l, r) -> l.getId().compareTo(r.getId()))
                .collect(toList());
    }

    @Data
    @Wither
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EstruturaOrganizacional {
        Servico servico;
        List<Unidade> unidades;
    }

    @Data
    @Wither
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Servico {
        long codigoErro;
        String mensagem;
    }

    @Data
    @Wither
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Unidade {
        String codigoUnidade;
        String codigoUnidadePai;
        String codigoOrgaoEntidade;
        String codigoTipoUnidade;
        String nome;
        String sigla;
        String codigoEsfera;
        String codigoPoder;
        String codigoNaturezaJuridica;
        String codigoSubNaturezaJuridica;
    }
}
