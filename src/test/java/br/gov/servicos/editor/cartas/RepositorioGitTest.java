package br.gov.servicos.editor.cartas;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.RefSpec;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.security.core.userdetails.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static br.gov.servicos.editor.utils.Unchecked.Supplier.uncheckedSupplier;
import static java.nio.file.Files.createTempDirectory;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.empty;
import static org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode.TRACK;
import static org.eclipse.jgit.lib.Constants.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class RepositorioGitTest {

    static File github;

    static {
        try {
            github = createTempDirectory("RepositorioGitTest-github").toFile();
            github.deleteOnExit();
            System.out.println("github = " + github);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    File clone;
    RepositorioGit repo;

    @BeforeClass
    public static void setUpOrigin() throws Exception {
        Git.cloneRepository()
                .setURI("https://github.com/servicosgovbr/cartas-de-servico.git")
                .setBare(true)
                .setDirectory(github)
                .setProgressMonitor(new TextProgressMonitor())
                .call();
    }

    @Before
    public void setUp() throws Exception {
        clone = createTempDirectory("RepositorioGitTest-clone").toFile();
        clone.deleteOnExit();

        Git.cloneRepository()
                .setURI(github.getAbsolutePath())
                .setDirectory(clone)
                .setProgressMonitor(new TextProgressMonitor())
                .call();

        System.out.println("clone = " + clone);

        this.repo = new RepositorioGit(clone, true);
    }

    @Test
    public void caminhosERevisoes() throws Exception {
        assertThat(repo.getCaminhoAbsoluto(), is(clone.toPath()));
        assertThat(repo.getRevisaoMaisRecenteDoArquivo(Paths.get("README.md")), is(not(empty())));
        assertThat(repo.getRevisaoMaisRecenteDoBranch("master"), is(not(empty())));
    }

    @Test
    public void fluxoDePublicacao() throws Exception {
        salvaAlteracao();
        garanteQueAlteracaoFoiParaGithub();

        salvaAlteracaoUsandoOutroClone();
        garanteQueAlteracaoFoiRecebida();

        salvaAlteracao();
        publicaAlteracao();
        garanteQueAlteracaoFoiPublicada();
    }

    @Test
    public void fluxoDeMoverBranch() throws Exception {
        moveBranch();
        garanteQueBranchFoiMovida();
    }

    private void moveBranch() throws IOException {
        User usuario = new User("Fulano de Tal", "", emptyList());

        repo.comRepositorioAbertoNoBranch("foo-bar", uncheckedSupplier(() -> {
            Path origem = Paths.get("README.md");
            Path destino = Paths.get("baz-bar.md");
            repo.moveBranchPara("baz-bar");
            Files.move(repo.getCaminhoAbsoluto().resolve(origem), repo.getCaminhoAbsoluto().resolve(destino));
            repo.remove(origem);
            repo.add(destino);
            repo.commit(origem, "Renomeia \"foo-bar\" para \"baz-bar\"", usuario);
            repo.commit(destino, "Renomeia \"foo-bar\" para \"baz-bar\"", usuario);
            repo.push("baz-bar");
            return null;
        }));
    }

    private void garanteQueBranchFoiMovida() {
        assertTrue(repo.branches().noneMatch(n -> n.equals("foo-bar")));
        assertTrue(repo.branches().anyMatch(n -> n.equals("baz-bar")));

        repo.comRepositorioAbertoNoBranch("baz-bar", uncheckedSupplier(() -> {
            Path antigo = repo.getCaminhoAbsoluto().resolve(Paths.get("README.md"));
            Path novo = repo.getCaminhoAbsoluto().resolve(Paths.get("baz-bar.md"));

            repo.pull();

            assertTrue(Files.notExists(antigo));
            assertTrue(Files.exists(novo));
            return null;
        }));
    }


    private void garanteQueAlteracaoFoiPublicada() throws IOException {
        try (Git git = Git.open(github)) {
            Ref master = git.getRepository().getRef(MASTER);
            assertThat(master, is(notNullValue()));

            RevCommit commit = new RevWalk(git.getRepository()).parseCommit(master.getObjectId());
            assertThat(commit.getAuthorIdent().getName(), is("fulano"));
            assertThat(commit.getAuthorIdent().getEmailAddress(), is("servicos@planejamento.gov.br"));
            assertThat(commit.getFullMessage(), is("Alteração de teste"));
        }
    }

    private void garanteQueAlteracaoFoiRecebida() throws IOException {
        repo.comRepositorioAbertoNoBranch("foo", uncheckedSupplier(() -> {
            Path relativo = Paths.get("README.md");
            Path absoluto = repo.getCaminhoAbsoluto().resolve(relativo);

            repo.pull();

            assertThat(Files.readAllLines(absoluto).get(0), is("# Teste 2"));

            return null;
        }));
    }

    private void salvaAlteracaoUsandoOutroClone() throws IOException, GitAPIException {
        File outroClone = createTempDirectory("RepositorioGitTest-outro").toFile();
        outroClone.deleteOnExit();

        try (Git git = Git.cloneRepository()
                .setURI(github.getAbsolutePath())
                .setDirectory(outroClone)
                .setProgressMonitor(new TextProgressMonitor())
                .setCloneAllBranches(true)
                .call()) {

            git.checkout().setName("foo").setCreateBranch(true).setUpstreamMode(TRACK).setStartPoint(R_REMOTES + DEFAULT_REMOTE_NAME + "/foo").call();
            assertThat(Files.readAllLines(outroClone.toPath().resolve("README.md")).get(0), is("# Teste"));

            Files.write(outroClone.toPath().resolve("README.md"), singletonList("# Teste 2"), WRITE);
            git.commit().setAll(true).setMessage("Commit de outro clone").call();
            git.push().setRefSpecs(new RefSpec("foo:foo")).setProgressMonitor(new TextProgressMonitor()).call();
        }
    }

    private void garanteQueAlteracaoFoiParaGithub() throws IOException {
        try (Git git = Git.open(github)) {
            Ref foo = git.getRepository().getRef("foo");
            assertThat(foo, is(notNullValue()));

            RevCommit commit = new RevWalk(git.getRepository()).parseCommit(foo.getObjectId());
            assertThat(commit.getAuthorIdent().getName(), is("fulano"));
            assertThat(commit.getAuthorIdent().getEmailAddress(), is("servicos@planejamento.gov.br"));
            assertThat(commit.getFullMessage(), is("Alteração de teste"));
        }
    }

    private void salvaAlteracao() throws IOException {
        repo.comRepositorioAbertoNoBranch("foo", uncheckedSupplier(() -> {
            Path relativo = Paths.get("README.md");
            Path absoluto = repo.getCaminhoAbsoluto().resolve(relativo);

            Files.write(absoluto, asList("# Teste", "\n", absoluto.toString()), WRITE);
            repo.commit(relativo, "Alteração de teste", new User("fulano", "123", emptyList()));

            repo.push("foo");

            return null;
        }));
    }

    private void publicaAlteracao() {
        repo.comRepositorioAbertoNoBranch(R_HEADS + MASTER, () -> {
            repo.pull();

            repo.merge(R_HEADS + "foo");
            repo.push(R_HEADS + MASTER);

            return null;
        });
    }
}