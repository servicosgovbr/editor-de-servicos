package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.cartas.Carta;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;

import java.util.function.Supplier;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RemoverCartaControllerTest {

    @Mock
    Carta carta;

    @Captor
    ArgumentCaptor<Supplier<Void>> captor;

    RemoverCartaController controller;

    public static final User USUARIO = new User("Fulano de Tal", "", emptyList());

    @Before
    public void setUp() throws Exception {
        controller = new RemoverCartaController();
    }

    @Test
    public void removeCartaExistente() throws Exception {
        controller.remover(carta, USUARIO);

        verify(carta).remover(USUARIO);
    }
}
