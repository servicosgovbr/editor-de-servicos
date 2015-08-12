package br.gov.servicos.editor.cartas;

import com.github.slugify.Slugify;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.eclipse.jgit.transport.RefSpec;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static lombok.AccessLevel.PRIVATE;
import static org.eclipse.jgit.lib.Constants.R_HEADS;

@FieldDefaults(level = PRIVATE, makeFinal = true)
public class Carta {

    @Getter
    String id;

    @SneakyThrows
    public static Carta id(String id) {
        return new Carta(new Slugify().slugify(id));
    }

    private Carta(String id) {
        this.id = id;
    }

    public Path caminhoAbsoluto(File raiz) {
        return Paths.get(raiz.getAbsolutePath(), "cartas-servico", "v3", "servicos", id + ".xml");
    }

    public RefSpec getRefSpec() {
        return new RefSpec(id + ":" + id);
    }


    public String getRef() {
        return R_HEADS + id;
    }

    public String caminhoRelativo(File raiz) {
        return raiz.toPath().relativize(caminhoAbsoluto(raiz)).toString();
    }
}
