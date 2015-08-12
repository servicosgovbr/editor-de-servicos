package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.cartas.Carta;
import br.gov.servicos.editor.cartas.Cartas;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static java.util.Locale.getDefault;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class ListarCartasController {

    Path v3;
    Cartas cartas;

    Formatter<Carta> formatter;

    @Autowired
    ListarCartasController(File repositorioCartasLocal, Cartas cartas, Formatter<Carta> formatter) {
        this.cartas = cartas;
        this.formatter = formatter;
        this.v3 = Paths.get(repositorioCartasLocal.getAbsolutePath(), "cartas-servico", "v3", "servicos");
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/servicos", method = GET)
    Iterable<Metadados> listar() throws IOException {
        return cartas.comRepositorioAberto(git -> {
            FilenameFilter filter = (x, name) -> name.endsWith(".xml");
            Function<Path, Carta> getId = f -> carta(formatter, f);
            Function<Path, Map<Carta, Path>> indexaServicos = f -> Arrays.asList(f.toFile().listFiles(filter))
                    .stream()
                    .map(File::toPath)
                    .collect(toMap(getId, x -> x));

            Map<Carta, Path> mapaServicos = indexaServicos.apply(v3);

            return mapaServicos.entrySet().stream()
                    .map(p -> p.getKey().metadados(git))
                    .filter(Objects::nonNull)
                    .collect(toList());
        });
    }

    @SneakyThrows
    private Carta carta(Formatter<Carta> factory, Path f) {
        return factory.parse(f.toFile().getName().replaceAll(".xml$", ""), getDefault());
    }

}
