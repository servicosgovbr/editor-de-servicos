package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.cartas.Carta;
import br.gov.servicos.editor.oauth2.google.api.UserProfiles;
import br.gov.servicos.editor.utils.ReformatadorXml;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.view.RedirectView;

import javax.xml.transform.dom.DOMSource;

import static br.gov.servicos.editor.utils.TestData.GOOGLE_PROFILE;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SalvarCartaControllerTest {

    public static final DOMSource DOM = new DOMSource();

    @Mock
    ReformatadorXml reformatadorXml;

    @Mock
    UserProfiles userProfiles;

    @Mock
    Carta carta;
    SalvarCartaController controller;

    @Before
    public void setUp() throws Exception {
        controller = new SalvarCartaController(reformatadorXml, userProfiles);
    }

    @Test
    public void deveReformatarAntesDeSalvar() throws Exception {
        given(userProfiles.get()).willReturn(GOOGLE_PROFILE);

        controller.salvar(carta, DOM);

        verify(reformatadorXml).formata(DOM);
    }

    @Test
    public void deveDelegarSalvamentoParaCarta() throws Exception {
        given(userProfiles.get()).willReturn(GOOGLE_PROFILE);
        given(reformatadorXml.formata(DOM)).willReturn("<servico/>");

        controller.salvar(carta, DOM);

        verify(carta).salvar(GOOGLE_PROFILE, "<servico/>");
    }

    @Test
    public void deveRedirecionarParaServico() throws Exception {
        given(reformatadorXml.formata(DOM)).willReturn("<servico/>");
        given(carta.getId()).willReturn("id-da-carta");

        RedirectView view = controller.salvar(carta, DOM);

        assertThat(view.getUrl(), is("/editar/api/servico/v3/id-da-carta"));
    }

}