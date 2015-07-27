package br.gov.servicos.editor.editar;

import br.gov.servicos.editor.servicos.Cartas;
import com.github.slugify.Slugify;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;

import javax.xml.transform.dom.DOMSource;
import java.util.ArrayList;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SalvarV3ControllerTest {

    @Mock
    Cartas cartas;

    @Test
    public void deveSalvarV3NasCartas() throws Exception {
        SalvarV3Controller controller = new SalvarV3Controller(cartas, new Slugify());

        String nome = "Servico Teste";
        DOMSource xml = new DOMSource();
        User user = new User("jean", "blah", new ArrayList());

        controller.salvar(nome, xml, user);
        verify(cartas).salvarServicoV3("servico-teste", xml, user);
    }
}