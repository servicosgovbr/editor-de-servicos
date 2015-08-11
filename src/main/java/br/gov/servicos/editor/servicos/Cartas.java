package br.gov.servicos.editor.servicos;

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
import org.eclipse.jgit.transport.RefSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.nio.charset.Charset.defaultCharset;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.*;
import static lombok.AccessLevel.PRIVATE;
import static org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode.NOTRACK;
import static org.eclipse.jgit.lib.Constants.*;

@Slf4j
@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class Cartas {


    File repositorioCartasLocal;
    Path v1;
    Path v3;
    boolean fazerPush;

    @Autowired
    public Cartas(File repositorioCartasLocal, @Value("${flags.git.push}") boolean fazerPush) {
        this.repositorioCartasLocal = repositorioCartasLocal;
        this.fazerPush = fazerPush;
        this.v1 = Paths.get(repositorioCartasLocal.getAbsolutePath(), "cartas-servico", "v1", "servicos");
        this.v3 = Paths.get(repositorioCartasLocal.getAbsolutePath(), "cartas-servico", "v3", "servicos");
    }

    @SneakyThrows
    public Optional<String> conteudoServicoV1(String id) {
        return conteudoServico(id, leitorDeConteudo(id, "v1"));
    }

    @SneakyThrows
    public Optional<String> conteudoServicoV3(String id) {
        return conteudoServico(id, leitorDeConteudo(id, "v3"));
    }

    public Optional<String> conteudoServico(String id, Supplier<Optional<String>> leitor) {
        return executaNoBranchDoServico(id, leitor);
    }

    public Supplier<Optional<String>> leitorDeConteudo(String id, String versao) {
        return () -> {
            File arquivo = Paths.get(repositorioCartasLocal.getAbsolutePath(), "cartas-servico", versao, "servicos", id + ".xml").toFile();
            if (arquivo.exists()) {
                log.info("Arquivo {} encontrado", arquivo);
                return ler(arquivo);
            }

            log.info("Arquivo {} não encontrado", arquivo);
            return empty();
        };
    }

    public Optional<Metadados> ultimaRevisaoV1(String id) {
        return comRepositorioAberto(git ->
                metadados(git, id, xmlServico(id, "v1")));
    }

    public Optional<Metadados> ultimaRevisaoV3(String id) {
        return comRepositorioAberto(git ->
                metadados(git, id, xmlServico(id, "v3")));
    }

    public Iterable<Metadados> listar() {
        return comRepositorioAberto(git -> todosServicos().stream()
                .map(p -> metadados(git, p.getKey(), p.getValue()))
                .map(Optional::get)
                .filter(Objects::nonNull)
                .collect(toList()));
    }

    @SneakyThrows
    public void excluir(String id, User usuario) {
        comRepositorioAberto(git -> {
            pull(git);
            try {
                executaNoBranchDoServico(id, () -> {
                    commit(git,
                            "Serviço deletado",
                            usuario,
                            excluirCarta(git, "v3", id),
                            excluirCarta(git, "v2", id),
                            excluirCarta(git, "v1", id));

                    return null;
                });
                return null;
            } finally {
                push(git, id);
            }
        });
    }


    @SneakyThrows
    private Path excluirCarta(Git git, String versao, String id) {
        Path caminho = caminhoAbsoluto(versao, id);
        if (!caminho.toFile().exists())
            return null;

        git.rm().addFilepattern(caminhoRelativo(caminho)).call();
        log.debug("git rm {}", caminho);

        return caminho;
    }

    private Set<Map.Entry<String, Path>> todosServicos() {
        FilenameFilter filter = (x, name) -> name.endsWith(".xml");
        Function<Path, String> getId = f -> f.toFile().getName().replaceAll(".xml$", "");
        Function<Path, Map<String, Path>> indexaServicos = f -> Arrays.asList(f.toFile().listFiles(filter))
                .stream()
                .map(File::toPath)
                .collect(toMap(getId, x -> x));

        Map<String, Path> mapaServicos = indexaServicos.apply(v1);
        mapaServicos.putAll(indexaServicos.apply(v3));

        return mapaServicos.entrySet();
    }

    @SneakyThrows
    private Optional<Metadados> metadados(Git git, String id, Path f) {
        RevCommit rev = Optional.ofNullable(git.getRepository().getRef(R_HEADS + id))
                .map(o -> {
                    try {
                        return git.log().add(o.getObjectId()).setMaxCount(1).call().iterator().next();
                    } catch (Throwable t) {
                        throw new RuntimeException(t);
                    }
                })
                .orElse(git.log().addPath(caminhoRelativo(f)).setMaxCount(1).call().iterator().next());

        return Optional.ofNullable(rev)
                .map(c -> new Metadados()
                        .withId(id)
                        .withRevisao(c.getId().getName())
                        .withAutor(c.getAuthorIdent().getName())
                        .withHorario(c.getAuthorIdent().getWhen()));
    }

    @SneakyThrows
    private Optional<String> ler(File arquivo) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), defaultCharset()))) {
            return of(reader.lines().collect(joining("\n")));
        }
    }

    @SneakyThrows
    public void salvarServicoV3(String id, String doc, User usuario) {
        comRepositorioAberto(git -> {

            pull(git);

            try {
                return executaNoBranchDoServico(id, () -> {
                    Path caminho = caminhoAbsoluto("v3", id);
                    Path dir = caminho.getParent();

                    if (dir.toFile().mkdirs()) {
                        log.debug("Diretório {} não existia e foi criado", dir);
                    } else {
                        log.debug("Diretório {} já existia e não precisou ser criado", dir);
                    }

                    String mensagem = format("%s '%s'", caminho.toFile().exists() ? "Altera" : "Cria", id);

                    escreveV3(doc, caminho);
                    add(git, caminho);
                    commit(git, mensagem, usuario, caminho);

                    return null;
                });

            } finally {
                push(git, id);
            }
        });
    }

    private Path caminhoAbsoluto(String versao, String id) {
        return Paths.get(repositorioCartasLocal.getAbsolutePath(), "cartas-servico", versao, "servicos", id + ".xml");
    }

    @SneakyThrows
    private void push(Git git, String id) {
        log.info("git push: {} ({})", git.getRepository().getBranch(), git.getRepository().getRepositoryState());
        if (fazerPush && !id.equals("novo")) {
            git.push()
                    .setRemote(DEFAULT_REMOTE_NAME)
                    .setRefSpecs(new RefSpec(id + ":" + id))
                    .setProgressMonitor(new TextProgressMonitor())
                    .call();
        } else {
            log.info("Envio de alterações ao Github desligado (FLAGS_GIT_PUSH=false)");
        }
    }

    @SneakyThrows
    private void pull(Git git) {
        log.info("git pull: {} ({})", git.getRepository().getBranch(), git.getRepository().getRepositoryState());
        git.pull()
                .setRebase(true)
                .setStrategy(MergeStrategy.THEIRS)
                .setProgressMonitor(new TextProgressMonitor())
                .call();
    }

    @SneakyThrows
    private void commit(Git git, String mensagem, User usuario, Path... caminhos) {
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
                    .forEach(p -> cmd.setOnly(caminhoRelativo(p)));

            cmd.call();
        } catch (JGitInternalException e) {
            if (e.getMessage().equals(JGitText.get().emptyCommit)) {
                log.info("{} não sofreu alterações", caminhos);
            } else {
                throw e;
            }
        }
    }

    @SneakyThrows
    private void add(Git git, Path path) {
        String pattern = caminhoRelativo(path);
        log.debug("git add: {} ({})", git.getRepository().getBranch(), git.getRepository().getRepositoryState(), pattern);

        git.add()
                .addFilepattern(pattern)
                .call();
    }

    private String caminhoRelativo(Path path) {
        return repositorioCartasLocal.toPath().relativize(path).toString();
    }

    @SneakyThrows
    private <T> T comRepositorioAberto(Function<Git, T> fn) {
        try (Git git = Git.open(repositorioCartasLocal)) {
            synchronized (Cartas.class) {
                return fn.apply(git);
            }
        }
    }

    @SneakyThrows
    private <T> T executaNoBranchDoServico(String id, Supplier<T> supplier) {
        return comRepositorioAberto(git -> {
            checkout(git, id);
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
    private void checkout(Git git, String id) {
        log.debug("git checkout: {} ({})", git.getRepository().getBranch(), git.getRepository().getRepositoryState(), id);

        git.checkout()
                .setName(id)
                .setStartPoint(R_HEADS + MASTER)
                .setUpstreamMode(NOTRACK)
                .setCreateBranch(!branchExiste(git, id))
                .call();
    }

    @SneakyThrows
    private boolean branchExiste(Git git, String id) {
        boolean resultado = git
                .branchList()
                .call()
                .stream()
                .anyMatch(b -> b.getName().equals(R_HEADS + id));

        log.debug("git branch {} já existe? {}", id, resultado);
        return resultado;
    }


    private Path xmlServico(String id, String versao) {
        return Paths.get(repositorioCartasLocal.getAbsolutePath(), "cartas-servico", versao, "servicos", id + ".xml");
    }

    @SneakyThrows
    private void escreveV3(String document, Path arquivo) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(arquivo.toFile()), "UTF-8")) {
            writer.write(document);
        }
        log.debug("Arquivo '{}' modificado", arquivo.getFileName());
    }

}
