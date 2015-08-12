package br.gov.servicos.editor.cartas;

import br.gov.servicos.editor.servicos.Metadados;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.RefSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Locale;

import static lombok.AccessLevel.PRIVATE;
import static org.eclipse.jgit.lib.Constants.R_HEADS;

@Slf4j
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class Carta {

    @Configuration
    public static class Config {

        @Bean
        public Formatter<Carta> formatter(File raiz) {
            return new Formatter<Carta>() {
                @Override
                public Carta parse(String text, Locale locale) {
                    return new Carta(text, raiz);
                }

                @Override
                public String print(Carta object, Locale locale) {
                    return object.toString();
                }
            };
        }
    }

    @Getter
    String id;

    File raiz;

    private Carta(String id, File raiz) {
        this.id = id;
        this.raiz = raiz;
    }

    public Path getCaminhoAbsoluto() {
        return Paths.get(raiz.getAbsolutePath(), "cartas-servico", "v3", "servicos", id + ".xml");
    }

    public RefSpec getRefSpec() {
        return new RefSpec(id + ":" + id);
    }


    public String getBranchRef() {
        return R_HEADS + id;
    }

    public Path getCaminhoRelativo() {
        return raiz.toPath().relativize(getCaminhoAbsoluto());
    }

    public Metadados metadados(Git git) {
        RevCommit commit = getCommitMaisRecente(git);

        return new Metadados()
                .withId(getId())
                .withRevisao(commit.getId().getName())
                .withAutor(commit.getAuthorIdent().getName())
                .withHorario(commit.getAuthorIdent().getWhen());
    }

    @SneakyThrows
    private RevCommit getCommitMaisRecente(Git git) {
        Iterator<RevCommit> commits;

        Ref ref = git.getRepository().getRef(getBranchRef());
        if (ref != null) {
            log.debug("Branch {} encontrado, pegando commit mais recente dele", getBranchRef());
            commits = git.log()
                    .add(ref.getObjectId())
                    .setMaxCount(1)
                    .call()
                    .iterator();
        } else {
            log.debug("Branch {} não encontrado, pegando commit mais recente do master", getBranchRef());
            commits = git.log()
                    .addPath(getCaminhoRelativo().toString())
                    .setMaxCount(1)
                    .call()
                    .iterator();
        }

        if (!commits.hasNext()) {
            throw new RuntimeException("Não foi possível determinar a última revisão de " + id);
        }

        return commits.next();
    }

}
