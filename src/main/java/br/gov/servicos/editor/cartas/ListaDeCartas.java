package br.gov.servicos.editor.cartas;

import br.gov.servicos.editor.servicos.Metadados;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.stream.Stream;

import static br.gov.servicos.editor.utils.Unchecked.Function.unchecked;
import static java.util.Locale.getDefault;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ListaDeCartas {

    RepositorioGit repositorioGit;
    Formatter<Carta> formatter;

    @Autowired
    public ListaDeCartas(RepositorioGit repositorioGit, Formatter<Carta> formatter) {
        this.repositorioGit = repositorioGit;
        this.formatter = formatter;
    }

    @PostConstruct
    public void esquentarCacheDeMetadados(Importador importador) throws Exception {
        if(importador.isImportadoComSucesso()) {
            listar();
            log.info("Cache de metadados das cartas criado com sucesso");
        }
        log.warn("Cache de metadados das cartas não foi criado - houve algum problema com o clone do repositório?");
    }

    public Iterable<Metadados> listar() throws FileNotFoundException, java.text.ParseException {
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
