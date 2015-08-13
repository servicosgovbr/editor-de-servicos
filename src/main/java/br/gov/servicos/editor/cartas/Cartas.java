package br.gov.servicos.editor.cartas;

import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.internal.JGitText;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.transport.RefSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import static lombok.AccessLevel.PRIVATE;
import static org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode.NOTRACK;
import static org.eclipse.jgit.lib.Constants.*;

@Slf4j
@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class Cartas {

    File repositorioCartasLocal;
    boolean fazerPush;

    @Autowired
    public Cartas(File repositorioCartasLocal, @Value("${flags.git.push}") boolean fazerPush) {
        this.repositorioCartasLocal = repositorioCartasLocal;
        this.fazerPush = fazerPush;
    }

    @SneakyThrows
    public void push(Git git, Carta carta) {
        log.info("git push: {} ({})", git.getRepository().getBranch(), git.getRepository().getRepositoryState());
        if (fazerPush) {
            git.push()
                    .setRemote(DEFAULT_REMOTE_NAME)
                    .setRefSpecs(new RefSpec(carta.getId() + ":" + carta.getId()))
                    .setProgressMonitor(new TextProgressMonitor())
                    .call();
        } else {
            log.info("Envio de alterações ao Github desligado (FLAGS_GIT_PUSH=false)");
        }
    }

    @SneakyThrows
    public void pull(Git git) {
        log.info("git pull: {} ({})", git.getRepository().getBranch(), git.getRepository().getRepositoryState());
        git.pull()
                .setRebase(true)
                .setStrategy(MergeStrategy.THEIRS)
                .setProgressMonitor(new TextProgressMonitor())
                .call();
    }

    @SneakyThrows
    public void commit(Git git, String mensagem, User usuario, Path... caminhos) {
        PersonIdent ident = new PersonIdent(usuario.getUsername(), "servicos@planejamento.gov.br");
        log.debug("git commit: {} ({}): '{}', {}, {}",
                git.getRepository().getBranch(),
                git.getRepository().getRepositoryState(),
                mensagem,
                ident,
                caminhos
        );

        try {
            CommitCommand cmd = git.commit()
                    .setMessage(mensagem)
                    .setCommitter(ident)
                    .setAuthor(ident);

            Arrays.asList(caminhos)
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(p -> cmd.setOnly(repositorioCartasLocal.toPath().relativize(p).toString()));

            cmd.call();
        } catch (JGitInternalException e) {
            if (e.getMessage().equals(JGitText.get().emptyCommit)) {
                log.info("Commit não possui alterações");
            } else {
                throw e;
            }
        }
    }

    @SneakyThrows
    public void add(Git git, Path path) {
        String pattern = repositorioCartasLocal.toPath().relativize(path).toString();
        log.debug("git add: {} ({})", git.getRepository().getBranch(), git.getRepository().getRepositoryState(), pattern);

        git.add()
                .addFilepattern(pattern)
                .call();
    }

    @SneakyThrows
    public <T> T comRepositorioAberto(Function<Git, T> fn) {
        try (Git git = Git.open(repositorioCartasLocal)) {
            synchronized (Git.class) {
                return fn.apply(git);
            }
        }
    }

    @SneakyThrows
    public <T> T executaNoBranchDoServico(Carta carta, Supplier<T> supplier) {
        return comRepositorioAberto(git -> {
            checkout(git, carta);
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
        git.checkout().setName(MASTER).call();
    }

    @SneakyThrows
    private void checkout(Git git, Carta carta) {
        log.debug("git checkout: {} ({})", git.getRepository().getBranch(), git.getRepository().getRepositoryState(), carta);

        git.checkout()
                .setName(carta.getId())
                .setStartPoint(R_HEADS + MASTER)
                .setUpstreamMode(NOTRACK)
                .setCreateBranch(!branchExiste(git, carta))
                .call();
    }

    @SneakyThrows
    private boolean branchExiste(Git git, Carta carta) {
        boolean resultado = git
                .branchList()
                .call()
                .stream()
                .anyMatch(b -> b.getName().equals(carta.getBranchRef()));

        log.debug("git branch {} já existe? {}", carta, resultado);
        return resultado;
    }


}
