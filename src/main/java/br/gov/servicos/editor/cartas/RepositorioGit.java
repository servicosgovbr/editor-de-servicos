package br.gov.servicos.editor.cartas;

import br.gov.servicos.editor.servicos.Metadados;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.RefNotAdvertisedException;
import org.eclipse.jgit.internal.JGitText;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.RefSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static br.gov.servicos.editor.utils.Unchecked.Function.unchecked;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static lombok.AccessLevel.PRIVATE;
import static org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode.NOTRACK;
import static org.eclipse.jgit.lib.Constants.*;
import static org.eclipse.jgit.merge.MergeStrategy.THEIRS;

@Slf4j
@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RepositorioGit {

    File raiz;
    boolean fazerPush;

    @NonFinal
    Git git; // disponível enquanto o repositório estiver aberto

    @Autowired
    public RepositorioGit(File repositorioCartasLocal, @Value("${flags.git.push}") boolean fazerPush) {
        this.raiz = repositorioCartasLocal;
        this.fazerPush = fazerPush;
    }

    public Path getCaminhoAbsoluto() {
        return raiz.getAbsoluteFile().toPath();
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

    @SneakyThrows
    private <T> T comRepositorioAberto(Function<Git, T> fn) {
        try (Git git = Git.open(raiz)) {
            synchronized (Git.class) {
                try {
                    this.git = git;
                    return fn.apply(git);
                } finally {
                    this.git = null;
                }
            }
        }
    }

    @SneakyThrows
    public <T> T comRepositorioAbertoNoBranch(String branch, Supplier<T> supplier) {
        return comRepositorioAberto(git -> {
            checkout(git, branch);
            try {
                return supplier.get();
            } finally {
                checkoutMaster(git);
            }
        });
    }

    @SneakyThrows
    private void checkoutMaster(Git git) {
        log.debug("git checkout master: {} ({})", git.getRepository().getBranch(), git.getRepository().getRepositoryState());
        git.checkout()
                .setName(MASTER)
                .call();
    }

    @SneakyThrows
    private void checkout(Git git, String branch) {
        log.debug("git checkout: {} ({})", git.getRepository().getBranch(), git.getRepository().getRepositoryState(), branch);

        git.checkout()
                .setName(branch.replaceAll("^" + R_HEADS, ""))
                .setStartPoint(R_HEADS + MASTER)
                .setUpstreamMode(NOTRACK)
                .setCreateBranch(!branchExiste(git, branch))
                .call();
    }

    @SneakyThrows
    private boolean branchExiste(Git git, String branch) {
        boolean resultado = git
                .branchList()
                .call()
                .stream()
                .anyMatch(b -> b.getName().equals(branch));

        log.debug("git branch {} já existe? {}", branch, resultado);
        return resultado;
    }

    public void pull() {
        try {
            log.info("git pull: {} ({})", git.getRepository().getBranch(), git.getRepository().getRepositoryState());

            git.pull()
                    .setRebase(true)
                    .setStrategy(THEIRS)
                    .setProgressMonitor(new TextProgressMonitor())
                    .call();

        } catch (RefNotAdvertisedException e) {
            try {
                git.pull()
                        .setRebase(true)
                        .setStrategy(THEIRS)
                        .setProgressMonitor(new TextProgressMonitor())
                        .setRemoteBranchName(MASTER)
                        .call();
            } catch (GitAPIException e1) {
                throw new RuntimeException(e1);
            }
        } catch (IOException | GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public void add(Path caminho) {
        log.debug("git add: {} ({})", git.getRepository().getBranch(), git.getRepository().getRepositoryState(), caminho);

        git.add()
                .addFilepattern(caminho.toString())
                .call();
    }

    @SneakyThrows
    public void commit(String mensagem, User usuario, Path caminho) {
        PersonIdent ident = new PersonIdent(usuario.getUsername(), "servicos@planejamento.gov.br");
        log.debug("git commit: {} ({}): '{}', {}, {}",
                git.getRepository().getBranch(),
                git.getRepository().getRepositoryState(),
                mensagem,
                ident,
                caminho
        );

        try {
            git.commit()
                    .setMessage(mensagem)
                    .setCommitter(ident)
                    .setAuthor(ident)
                    .setOnly(caminho.toString())
                    .call();

        } catch (JGitInternalException e) {
            if (e.getMessage().equals(JGitText.get().emptyCommit)) {
                log.info("Commit não possui alterações");
            } else {
                throw e;
            }
        }
    }

    @SneakyThrows
    public void push(String branch) {
        log.info("git push: {} ({})", git.getRepository().getBranch(), git.getRepository().getRepositoryState());
        if (fazerPush) {
            git.push()
                    .setRemote(DEFAULT_REMOTE_NAME)
                    .setRefSpecs(new RefSpec(branch + ":" + branch))
                    .setProgressMonitor(new TextProgressMonitor())
                    .call();
        } else {
            log.info("Envio de alterações ao Github desligado (FLAGS_GIT_PUSH=false)");
        }
    }

    @SneakyThrows
    public void remove(Path caminho) {
        if (!caminho.toFile().exists()) {
            log.debug("Caminho {} não existe, e não pode ser removido", caminho);
            return;
        }

        git.rm().addFilepattern(caminho.toString()).call();
        log.debug("git rm {}", caminho);
    }
}
