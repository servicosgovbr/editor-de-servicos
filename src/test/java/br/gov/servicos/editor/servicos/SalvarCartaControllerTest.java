package br.gov.servicos.editor.servicos;

import com.github.slugify.Slugify;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;

import javax.xml.transform.dom.DOMSource;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SalvarCartaControllerTest {

    @Mock
    Cartas cartas;

    @Mock
    DOMSource source;

    @Test
    public void deveSalvarV3NasCartas() throws Exception {
        SalvarCartaController controller = new SalvarCartaController(cartas, new Slugify());

        String nome = "Servico Teste";
        User user = new User("jean", "blah", emptyList());

        controller.salvar(nome, source, user);
        verify(cartas).salvarServico("servico-teste", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n\n", user);
    }
}