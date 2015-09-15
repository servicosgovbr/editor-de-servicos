package br.gov.servicos.editor.cartas;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
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
import static java.util.Optional.empty;
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
                .setURI("https://github.com/servicosgovbr/cartas-de-servico.git")
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

        try (Git originGit = Git.open(origin)) {
            Ref foo = originGit.getRepository().getRef("foo");
            assertThat(foo, is(notNullValue()));

            RevCommit commit = new RevWalk(originGit.getRepository()).parseCommit(foo.getObjectId());
            assertThat(commit.getAuthorIdent().getName(), is("fulano"));
            assertThat(commit.getAuthorIdent().getEmailAddress(), is("servicos@planejamento.gov.br"));
            assertThat(commit.getFullMessage(), is("Alteração de teste"));
        }
    }
}