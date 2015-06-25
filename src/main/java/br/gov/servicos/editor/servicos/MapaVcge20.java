package br.gov.servicos.editor.servicos;

import com.github.slugify.Slugify;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.*;

@Slf4j
public class MapaVcge20 {


    private Map<String, Set<AreaDeInteresse>> mapaVcge;

    @SneakyThrows
    public MapaVcge20(ClassPathResource areasInteresseCsv) {
        this.mapaVcge = carregaMapa(areasInteresseCsv);
    }

    private Map<String, Set<AreaDeInteresse>> carregaMapa(ClassPathResource areasInteresseCsv) throws IOException {
        Slugify slug = new Slugify();

        try (CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(IO.bufferedReader(areasInteresseCsv))) {
            return parser.getRecords().stream()
                    .collect(
                            groupingBy(
                                    r -> r.get("from"),
                                    mapping(r -> new AreaDeInteresse()
                                                    .withId(slug.slugify(r.get("area") + "-" + r.get("subArea")))
                                                    .withArea(r.get("area").trim())
                                                    .withSubArea(r.get("subArea").trim()),
                                            toSet())));
        }
    }

    public Set<AreaDeInteresse> areaDeInteresse(String id, String areaInteresse) {
        log.info(areaInteresse);

        return Optional
                .ofNullable(mapaVcge.get(id))
                .orElse(new HashSet<AreaDeInteresse>() {{
                    add(new AreaDeInteresse()
                            .withId(id)
                            .withArea(areaInteresse));
                }});
    }
}
