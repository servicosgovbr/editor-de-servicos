package br.gov.servicos.editor.cartas;

import br.gov.servicos.editor.servicos.Metadados;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;

import static br.gov.servicos.editor.utils.Unchecked.Function.unchecked;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RepositorioGit {

    File raiz;

    @Autowired
    public RepositorioGit(File repositorioCartasLocal) {
        raiz = repositorioCartasLocal;
    }

    public Path getCaminhoAbsoluto() {
        return raiz.getAbsoluteFile().toPath();
    }

    @SneakyThrows
    public <T> T comRepositorioAberto(Function<Git, T> fn) {
        try (Git git = Git.open(raiz)) {
            synchronized (Git.class) {
                return fn.apply(git);
            }
        }
    }

    public Optional<Metadados> getCommitMaisRecenteDoArquivo(Path caminhoRelativo) {
        return comRepositorioAberto(unchecked(git -> {
            log.debug("Branch não encontrado, pegando commit mais recente do arquivo {} no master", caminhoRelativo);
            Iterator<RevCommit> commits = git.log()
                    .addPath(caminhoRelativo.toString())
                    .setMaxCount(1)
                    .call()
                    .iterator();

            return metadadosDoCommitMaisRecente(commits);
        }));
    }

    public Optional<Metadados> getCommitMaisRecenteDoBranch(String branch) {
        return comRepositorioAberto(unchecked(git -> {
            Ref ref = git.getRepository().getRef(branch);

            if (ref == null) {
                log.debug("Branch {} não encontrado", branch);
                return empty();
            }

            log.debug("Branch {} encontrado, pegando commit mais recente dele", ref);
            Iterator<RevCommit> commits = git.log()
                    .add(ref.getObjectId())
                    .setMaxCount(1)
                    .call()
                    .iterator();

            return metadadosDoCommitMaisRecente(commits);
        }));
    }

    private Optional<Metadados> metadadosDoCommitMaisRecente(Iterator<RevCommit> commits) {
        if (!commits.hasNext()) {
            return empty();
        }

        RevCommit commit = commits.next();
        return of(new Metadados()
                .withRevisao(commit.getId().getName())
                .withAutor(commit.getAuthorIdent().getName())
                .withHorario(commit.getAuthorIdent().getWhen()));
    }
}
