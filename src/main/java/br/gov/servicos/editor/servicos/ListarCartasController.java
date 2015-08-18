package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.cartas.Carta;
import br.gov.servicos.editor.cartas.RepositorioGit;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static br.gov.servicos.editor.utils.Unchecked.Function.unchecked;
import static java.util.Locale.getDefault;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@FieldDefaults(level = PRIVATE, makeFinal = true)
class ListarCartasController {

    RepositorioGit repositorioGit;
    Formatter<Carta> formatter;

    @Autowired
    ListarCartasController(RepositorioGit repositorioGit, Formatter<Carta> formatter) {
        this.repositorioGit = repositorioGit;
        this.formatter = formatter;
    }

    @ResponseBody
    @RequestMapping(value = "/editar/api/servicos", method = GET)
    Iterable<Metadados> listar() throws IOException {
        File dir = repositorioGit.getCaminhoAbsoluto().resolve("cartas-servico/v3/servicos").toFile();

        if (!dir.exists()) {
            throw new FileNotFoundException("Diretório " + dir + " não encontrado!");
        }

        File[] arquivos = Optional
                .ofNullable(dir.listFiles((x, name) -> name.endsWith(".xml")))
                .orElse(new File[0]);

        return Stream.of(arquivos)
                .map(f -> f.getName().replaceAll(".xml$", ""))
                .map(unchecked(id -> formatter.parse(id, getDefault())))
                .map(Carta::getMetadados)
                .collect(toList());
    }

}
