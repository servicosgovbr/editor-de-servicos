package br.gov.servicos.editor.servicos;

import com.github.slugify.Slugify;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MapaVcge20 {

    Slugify slugify;
    Map<String, Set<String>> mapaVcge;

    @SneakyThrows
    public MapaVcge20(ClassPathResource areasInteresseCsv, Slugify slugify) {
        this.slugify = slugify;
        this.mapaVcge = carregaMapa(areasInteresseCsv);
    }

    private Map<String, Set<String>> carregaMapa(ClassPathResource areasInteresseCsv) throws IOException {
        try (CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(IO.bufferedReader(areasInteresseCsv))) {
            return parser.getRecords().stream()
                    .collect(groupingBy(
                            r -> r.get("from"),
                            mapping(r -> r.get("area").trim(), toSet())));
        }
    }

    public Set<String> areaDeInteresse(String areaInteresse) {
        return Optional
                .ofNullable(mapaVcge.get(slugify.slugify(areaInteresse)))
                .orElse(new HashSet<String>() {{
                    add(areaInteresse);
                }});
    }
}
