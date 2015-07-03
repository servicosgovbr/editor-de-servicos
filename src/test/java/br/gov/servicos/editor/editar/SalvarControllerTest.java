package br.gov.servicos.editor.editar;

import br.gov.servicos.editor.frontend.ExportadorServicoV2;
import br.gov.servicos.editor.servicos.Cartas;
import br.gov.servicos.editor.servicos.Servico;
import br.gov.servicos.editor.usuarios.Papeis;
import com.github.slugify.Slugify;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.servlet.view.RedirectView;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SalvarControllerTest {

    SalvarController controller;

    @Mock
    ExportadorServicoV2 exportadorV2;

    @Mock
    Cartas cartas;

    @Before
    public void setUp() throws Exception {
        controller = new SalvarController(exportadorV2, cartas, new Slugify());
    }

    @Test
    public void deveSalvarServico() throws Exception {
        Servico servico = new Servico().withNome("Passaporte");
        User usuario = new User("fulano", "segredo123", asList(new SimpleGrantedAuthority(Papeis.ADMIN)));
        Document doc = new Document("servico-v2");

        when(exportadorV2.exportar(servico)).thenReturn(doc);

        RedirectView redirect = controller.salvar(servico, usuario);

        assertThat(redirect.getUrl(), is("/editar/servico/passaporte"));

        verify(cartas).salvarServicoV2("passaporte", doc, usuario);
    }

}