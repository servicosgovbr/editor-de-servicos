package br.gov.servicos.editor.servicos;

import com.github.slugify.Slugify;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class ListarCartasController {

    Path v3;
    File repositorioCartasLocal;
    Cartas cartas;
    Slugify slugify;

    @Autowired
    ListarCartasController(File repositorioCartasLocal, Cartas cartas, Slugify slugify) {
        this.repositorioCartasLocal = repositorioCartasLocal;
        this.cartas = cartas;
        this.slugify = slugify;
        this.v3 = Paths.get(repositorioCartasLocal.getAbsolutePath(), "cartas-servico", "v3", "servicos");
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/servicos", method = GET)
    Iterable<Metadados> listar() throws IOException {
        return cartas.comRepositorioAberto(git -> {
            FilenameFilter filter = (x, name) -> name.endsWith(".xml");
            Function<Path, String> getId = f -> f.toFile().getName().replaceAll(".xml$", "");
            Function<Path, Map<String, Path>> indexaServicos = f -> Arrays.asList(f.toFile().listFiles(filter))
                    .stream()
                    .map(File::toPath)
                    .collect(toMap(getId, x -> x));

            Map<String, Path> mapaServicos = indexaServicos.apply(v3);

            return mapaServicos.entrySet().stream()
                    .map(p -> cartas.metadados(git, p.getKey(), p.getValue()))
                    .map(Optional::get)
                    .filter(Objects::nonNull)
                    .collect(toList());
        });
    }

}
