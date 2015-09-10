package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.cartas.Carta;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PublicarCartaControllerTest {

    public static final User USUARIO = new User("Fulano de Tal", "", emptyList());

    @Mock
    Carta carta;

    PublicarCartaController controller;

    @Before
    public void setUp() throws Exception {
        controller = new PublicarCartaController();
    }

    @Test
    public void publicaCartaExistente() throws Exception {
        controller.publicar(carta);
        verify(carta).publicar();
    }

}