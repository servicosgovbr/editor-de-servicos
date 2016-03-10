package br.gov.servicos.editor.frontend;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.function.Predicate;
import java.util.zip.GZIPInputStream;

import static br.gov.servicos.editor.frontend.Siorg.Unidade;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;
import static net.logstash.logback.marker.Markers.append;

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
    public Orgaos(@Qualifier("restTemplate") RestTemplate rest, ObjectMapper mapper, Slugify slugify) {
        this.rest = rest;
        this.slugify = slugify;
        reader = mapper.reader(EstruturaOrganizacional.class);
    }

    @Override
    @SneakyThrows
    public void afterPropertiesSet() throws Exception {
        log.debug("Lendo estrutura organizacional de {}...", RESOURCE);
        estruturaOrganizacional = reader.readValue(new GZIPInputStream(RESOURCE.getInputStream()));
        log.debug("Estrutura organizacional lida com sucesso", RESOURCE);
    }

    @Cacheable("orgaos")
    public List<OrgaoDTO> get(String termo) {
        List<OrgaoDTO> busca = estruturaOrganizacional.getUnidades()
                .stream()
                .filter(new FiltroDeOrgaos(termo))
                .map(u -> new OrgaoDTO().withNome(String.format("%s (%s)", u.getNome().trim(), u.getSigla().trim()))
                        .withId(u.getCodigoUnidade()))
                .sorted(comparing(OrgaoDTO::getNome))
                .collect(toList());

        log.info(append("orgaos.termo", termo).and(append("orgaos.resultados", busca.size())),
                "Buscando órgãos com termo '{}': {} resultados", termo, busca.size());

        return busca;
    }

    @Data
    @Wither
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EstruturaOrganizacional {
        List<Unidade> unidades;
    }


    @FieldDefaults(level = PRIVATE, makeFinal = true)
    private class FiltroDeOrgaos implements Predicate<Unidade> {

        String termo;

        public FiltroDeOrgaos(String termo) {
            this.termo = termo;
        }

        @Override
        public boolean test(Unidade u) {
            return limpa(u.getNome()).contains(limpa(termo)) || limpa(u.getSigla()).contains(limpa(termo));
        }

        private String limpa(String s) {
            return slugify.slugify(s).replace('-', ' ');
        }

    }
}
