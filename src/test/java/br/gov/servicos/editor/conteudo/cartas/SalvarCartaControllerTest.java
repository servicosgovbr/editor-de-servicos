package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.ConteudoVersionadoFactory;
import br.gov.servicos.editor.security.UserProfiles;
import br.gov.servicos.editor.utils.ReformatadorXml;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.view.RedirectView;

import javax.xml.transform.dom.DOMSource;

import static br.gov.servicos.editor.conteudo.TipoPagina.SERVICO;
import static br.gov.servicos.editor.utils.TestData.PROFILE;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SalvarCartaControllerTest {

    public static final DOMSource DOM = new DOMSource();

    @Mock
    ReformatadorXml reformatadorXml;

    @Mock
    UserProfiles userProfiles;

    @Mock
    ConteudoVersionadoFactory factory;

    @Mock
    ConteudoVersionado carta;

    SalvarCartaController controller;

    @Before
    public void setUp() throws Exception {
        controller = new SalvarCartaController(reformatadorXml, userProfiles, factory);
        given(factory.pagina(anyString(), eq(SERVICO)))
                .willReturn(carta);
    }

    @Test
    public void deveReformatarAntesDeSalvar() throws Exception {
        given(userProfiles.get()).willReturn(PROFILE);
        controller.salvar("servico", "", DOM);

        verify(reformatadorXml).formata(DOM);
    }

    @Test
    public void deveDelegarSalvamentoParaCarta() throws Exception {
        given(userProfiles.get()).willReturn(PROFILE);
        given(reformatadorXml.formata(DOM)).willReturn("<servico/>");

        controller.salvar("servico", "", DOM);

        verify(carta).salvar(PROFILE, "<servico/>");
    }

    @Test
    public void deveRedirecionarParaServico() throws Exception {
        given(reformatadorXml.formata(DOM)).willReturn("<servico/>");
        given(carta.getId()).willReturn("id-da-carta");

        RedirectView view = controller.salvar("servico", "", DOM);

        assertThat(view.getUrl(), is("/editar/api/pagina/servico/id-da-carta"));
    }

}