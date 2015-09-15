package br.gov.servicos.editor.cartas;

import org.eclipse.jgit.api.Git;
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
import static org.eclipse.jgit.lib.Constants.DEFAULT_REMOTE_NAME;
import static org.eclipse.jgit.lib.Constants.R_REMOTES;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class RepositorioGitTest {

    static File origin;

    static {
        try {
            origin = createTempDirectory("RepositorioGitTest-origin").toFile();
            origin.deleteOnExit();
            System.out.println("origin = " + origin);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    File clone;
    RepositorioGit repo;

    @BeforeClass
    public static void setUpOrigin() throws Exception {
        Git.cloneRepository()
                .setURI("/Users/cvillela/src/servicos.gov.br/cartas-de-servico")
                .setBare(true)
                .setDirectory(origin)
                .setProgressMonitor(new TextProgressMonitor())
                .call();
    }

    @Before
    public void setUp() throws Exception {
        clone = createTempDirectory("RepositorioGitTest-clone").toFile();
        clone.deleteOnExit();

        Git.cloneRepository()
                .setURI(origin.getAbsolutePath())
                .setDirectory(clone)
                .setProgressMonitor(new TextProgressMonitor())
                .call();

        System.out.println("clone = " + clone);

        this.repo = new RepositorioGit(clone, true);
    }

    @Test
    public void caminhoAbsoluto() throws Exception {
        assertThat(repo.getCaminhoAbsoluto(), is(clone.toPath()));
    }

    @Test
    public void revisaoMaisRecenteDeArquivo() throws Exception {
        assertThat(repo.getRevisaoMaisRecenteDoArquivo(Paths.get("README.md")), is(not(empty())));
    }

    @Test
    public void revisaoMaisRecenteDeBranch() throws Exception {
        assertThat(repo.getRevisaoMaisRecenteDoBranch("master"), is(not(empty())));
    }

    @Test
    public void pushAposCommit() throws Exception {
        repo.comRepositorioAbertoNoBranch("foo", uncheckedSupplier(() -> {
            Path relativo = Paths.get("README.md");
            Path absoluto = repo.getCaminhoAbsoluto().resolve(relativo);

            Files.write(absoluto, asList("# Teste", "\n", absoluto.toString()), WRITE);
            repo.commit(relativo, "Alteração de teste", new User("fulano", "123", emptyList()));

            repo.push("foo");

            return null;
        }));

        try (Git git = Git.open(origin)) {
            Ref foo = git.getRepository().getRef("foo");
            assertThat(foo, is(notNullValue()));

            RevCommit commit = new RevWalk(git.getRepository()).parseCommit(foo.getObjectId());
            assertThat(commit.getAuthorIdent().getName(), is("fulano"));
            assertThat(commit.getAuthorIdent().getEmailAddress(), is("servicos@planejamento.gov.br"));
            assertThat(commit.getFullMessage(), is("Alteração de teste"));
        }

        File outroClone = createTempDirectory("RepositorioGitTest-origin").toFile();
        outroClone.deleteOnExit();

        try (Git git = Git.cloneRepository()
                .setURI(origin.getAbsolutePath())
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

        repo.comRepositorioAbertoNoBranch("foo", uncheckedSupplier(() -> {
            Path relativo = Paths.get("README.md");
            Path absoluto = repo.getCaminhoAbsoluto().resolve(relativo);

            repo.pull();

            assertThat(Files.readAllLines(absoluto).get(0), is("# Teste 2"));

            return null;
        }));
    }
}