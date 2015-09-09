package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.cartas.Carta;
import br.gov.servicos.editor.utils.ReformatadorXml;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.servlet.view.RedirectView;

import javax.xml.transform.dom.DOMSource;

import static java.util.Collections.emptyList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PublicarCartaControllerTest {

    @Mock
    Carta carta;

    PublicarCartaController controller;

    public static final User USUARIO = new User("Fulano de Tal", "", emptyList());

    @Before
    public void setUp() throws Exception {
        controller = new PublicarCartaController();
    }

    @Test
    public void publicaCartaExistente() throws Exception {
        controller.publicar(carta, USUARIO);
        assertTrue(true);
    }

}