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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SalvarCartaControllerTest {

    @Mock
    ReformatadorXml reformatadorXml;

    @Mock
    Carta carta;

    SalvarCartaController controller;

    public static final User USUARIO = new User("Fulano de Tal", "", emptyList());
    public static final DOMSource DOM = new DOMSource();

    @Before
    public void setUp() throws Exception {
        controller = new SalvarCartaController(reformatadorXml);
    }

    @Test
    public void deveReformatarAntesDeSalvar() throws Exception {
        controller.salvar(carta, DOM, USUARIO);

        verify(reformatadorXml).formata(DOM);
    }

    @Test
    public void deveDelegarSalvamentoParaCarta() throws Exception {
        given(reformatadorXml.formata(DOM)).willReturn("<servico/>");

        controller.salvar(carta, DOM, USUARIO);

        verify(carta).salvar(USUARIO, "<servico/>");
    }

    @Test
    public void deveRedirecionarParaServico() throws Exception {
        given(reformatadorXml.formata(DOM)).willReturn("<servico/>");
        given(carta.getId()).willReturn("id-da-carta");

        RedirectView view = controller.salvar(carta, DOM, USUARIO);

        assertThat(view.getUrl(), is("/editar/api/servico/v3/id-da-carta"));
    }

}