package br.gov.servicos.editor.cartas;

import br.gov.servicos.editor.servicos.Revisao;
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
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.treewalk.filter.AndTreeFilter;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
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
import static org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode.TRACK;
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

    public Optional<Revisao> getRevisaoMaisRecenteDoArquivo(Path caminhoRelativo) {
        RevCommit commit = comRepositorioAbertoParaLeitura(unchecked(git -> {

            Repository repo = git.getRepository();
            RevWalk revWalk = new RevWalk(repo);

            revWalk.setTreeFilter(AndTreeFilter.create(PathFilter.create(caminhoRelativo.toString()), TreeFilter.ANY_DIFF));
            revWalk.markStart(revWalk.lookupCommit(repo.resolve(HEAD)));

            Iterator<RevCommit> revs = revWalk.iterator();
            if (revs.hasNext()) {
                return revs.next();
            }
            return null;
        }));

        if (commit == null) {
            log.debug("Arquivo {} @ master não encontrado", caminhoRelativo);
            return empty();
        }

        log.debug("Arquivo {} @ master: {}", caminhoRelativo, commit.getId().getName());
        return of(new Revisao(commit));
    }

    public Optional<Revisao> getRevisaoMaisRecenteDoBranch(String branch) {
        RevCommit commit = comRepositorioAbertoParaLeitura(unchecked(git -> {
            Repository repo = git.getRepository();
            Ref ref = repo.getRef(branch);

            if (ref == null) {
                return null;
            }

            return new RevWalk(repo).parseCommit(ref.getObjectId());
        }));

        if (commit == null) {
            log.debug("Branch {} não encontrado", branch);
            return empty();

        }

        log.debug("Branch {}: {}", branch, commit.getId().getName());
        return of(new Revisao(commit));
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
    private <T> T comRepositorioAbertoParaLeitura(Function<Git, T> fn) {
        try (Git git = Git.open(raiz)) {
            try {
                this.git = git;
                return fn.apply(git);
            } finally {
                this.git = null;
            }
        }
    }

    @SneakyThrows
    public <T> T comRepositorioAbertoNoBranch(String branch, Supplier<T> supplier) {
        return comRepositorioAberto(git -> {
            checkout(branch);
            try {
                return supplier.get();
            } finally {
                checkoutMaster();
            }
        });
    }

    @SneakyThrows
    private void checkoutMaster() {
        log.debug("git checkout: {} -> {}", git.getRepository().getFullBranch(), R_HEADS + MASTER);
        git.checkout()
                .setName(R_HEADS + MASTER)
                .call();
    }

    @SneakyThrows
    private void checkout(String branch) {
        log.debug("git checkout: {} -> {}", git.getRepository().getFullBranch(), branch);

        if (git.getRepository().getRefDatabase().getRef(branch) == null) {
            git.checkout()
                    .setName(branch.replaceAll("^" + R_HEADS, ""))
                    .setStartPoint(R_HEADS + MASTER)
                    .setUpstreamMode(TRACK)
                    .setCreateBranch(true)
                    .call();
        } else {
            git.checkout()
                    .setName(branch.replaceAll("^" + R_HEADS, ""))
                    .setCreateBranch(false)
                    .call();
        }
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
    public void commit(Path caminho, String mensagem, User usuario) {
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
        git.rm().addFilepattern(caminho.toString()).call();
        log.debug("git rm {}", caminho);
    }
}
