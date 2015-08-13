package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.cartas.Carta;
import br.gov.servicos.editor.cartas.RepositorioGit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

import static java.util.Collections.emptyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RemoverCartaControllerTest {

    @Mock
    RepositorioGit repositorio;

    @Mock
    Carta carta;

    @Captor
    ArgumentCaptor<Supplier<Void>> captor;

    RemoverCartaController controller;

    public static final User USUARIO = new User("Fulano de Tal", "", emptyList());

    @Before
    public void setUp() throws Exception {
        controller = new RemoverCartaController(repositorio);
    }

    @Test
    public void removeCartaExistente() throws Exception {
        given(carta.getBranchRef())
                .willReturn("refs/heads/id-a-deletar");

        given(repositorio.comRepositorioAbertoNoBranch(eq("refs/heads/id-a-deletar"), captor.capture()))
                .willReturn(null);

        controller.remover(carta, USUARIO);

        Path caminho = Paths.get("cartas-servico/v3/servicos/id-a-deletar.xml");

        given(carta.getCaminhoRelativo())
                .willReturn(caminho);

        captor.getValue().get();

        verify(repositorio).pull();
        verify(repositorio).remove(caminho);
        verify(repositorio).push("refs/heads/id-a-deletar");
    }
}
