package br.gov.servicos.editor.cartas;

import br.gov.servicos.editor.servicos.Metadados;
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
import org.eclipse.jgit.revwalk.RevCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.nio.charset.Charset.defaultCharset;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.joining;
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

    public Supplier<Optional<String>> leitor(Carta carta) {
        return () -> {
            File arquivo = carta.caminhoAbsoluto().toFile();
            if (arquivo.exists()) {
                log.info("Arquivo {} encontrado", arquivo);
                return ler(arquivo);
            }

            log.info("Arquivo {} não encontrado", arquivo);
            return empty();
        };
    }

    @SneakyThrows
    public Optional<Metadados> metadados(Git git, Carta carta) {
        return metadados(git, carta, carta.caminhoAbsoluto());
    }

    @SneakyThrows
    public Optional<Metadados> metadados(Git git, Carta carta, Path f) {
        RevCommit rev = Optional.ofNullable(git.getRepository().getRef(carta.getBranchRef()))
                .map(o -> {
                    try {
                        return git.log().add(o.getObjectId()).setMaxCount(1).call().iterator().next();
                    } catch (Throwable t) {
                        throw new RuntimeException(t);
                    }
                })
                .orElseGet(getRevCommitSupplier(git, carta));

        return Optional.ofNullable(rev)
                .map(c -> new Metadados()
                        .withId(carta.getId())
                        .withRevisao(c.getId().getName())
                        .withAutor(c.getAuthorIdent().getName())
                        .withHorario(c.getAuthorIdent().getWhen()));
    }

    @SneakyThrows
    private Supplier<RevCommit> getRevCommitSupplier(Git git, Carta carta) {
        return () -> getNextLog(git, carta);
    }

    @SneakyThrows
    private RevCommit getNextLog(Git git, Carta carta) {
        return git.log()
                .addPath(carta.caminhoRelativo().toString())
                .setMaxCount(1)
                .call()
                .iterator()
                .next();
    }

    @SneakyThrows
    private Optional<String> ler(File arquivo) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), defaultCharset()))) {
            return of(reader.lines().collect(joining("\n")));
        }
    }

    @SneakyThrows
    public void push(Git git, Carta carta) {
        log.info("git push: {} ({})", git.getRepository().getBranch(), git.getRepository().getRepositoryState());
        if (fazerPush) {
            git.push()
                    .setRemote(DEFAULT_REMOTE_NAME)
                    .setRefSpecs(carta.getRefSpec())
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
            synchronized (Cartas.class) {
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


    @SneakyThrows
    public void escrever(String document, Path arquivo) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(arquivo.toFile()), "UTF-8")) {
            writer.write(document);
        }
        log.debug("Arquivo '{}' modificado", arquivo.getFileName());
    }

}
