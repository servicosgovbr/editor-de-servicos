package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.ConteudoVersionadoFactory;
import br.gov.servicos.editor.conteudo.TipoPagina;
import br.gov.servicos.editor.frontend.Siorg;
import br.gov.servicos.editor.security.TipoPermissao;
import br.gov.servicos.editor.security.UserProfiles;
import br.gov.servicos.editor.utils.ReformatadorXml;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.view.RedirectView;

import javax.xml.transform.dom.DOMSource;

import static br.gov.servicos.editor.conteudo.TipoPagina.SERVICO;
import static br.gov.servicos.editor.utils.TestData.PROFILE;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SalvarCartaControllerTest {

    public static final DOMSource DOM = new DOMSource();

    public static final String CARTA = "<servico><url>http://estruturaorganizacional.dados.gov.br/id/unidade-organizacional/1934</url><nome>Carta A</nome></servico>";

    @Mock
    ReformatadorXml reformatadorXml;

    @Mock
    UserProfiles userProfiles;

    @Mock
    ConteudoVersionadoFactory factory;

    @Mock
    ConteudoVersionado carta;

    @Autowired
    Siorg siorg;

    SalvarCartaController controller;

    @Before
    public void setUp() throws Exception {
        controller = new SalvarCartaController(reformatadorXml, userProfiles, factory, siorg);
        given(factory.pagina(anyString(), eq(SERVICO)))
                .willReturn(carta);
    }

    @Test
    public void deveReformatarAntesDeSalvar() throws Exception {
        given(userProfiles.get()).willReturn(PROFILE);
        given(userProfiles.temPermissaoParaTipoPaginaOrgaoEspecifico(eq(TipoPermissao.EDITAR_SALVAR), any(TipoPagina.class), anyString())).willReturn(true);
        controller.salvar("servico", "", DOM);

        verify(reformatadorXml).formata(DOM);
    }

    @Test
    public void deveDelegarSalvamentoParaCarta() throws Exception {
        given(userProfiles.get()).willReturn(PROFILE);
        given(userProfiles.temPermissaoParaTipoPaginaOrgaoEspecifico(eq(TipoPermissao.EDITAR_SALVAR), any(TipoPagina.class), anyString())).willReturn(true);
        given(reformatadorXml.formata(DOM)).willReturn("<servico/>");

        controller.salvar("servico", "", DOM);

        verify(carta).salvar(PROFILE, "<servico/>");
    }

    @Test
    public void deveRedirecionarParaServico() throws Exception {
        given(reformatadorXml.formata(DOM)).willReturn("<servico/>");
        given(carta.getId()).willReturn("id-da-carta");
        given(userProfiles.temPermissaoParaTipoPaginaOrgaoEspecifico(eq(TipoPermissao.EDITAR_SALVAR), any(TipoPagina.class), anyString())).willReturn(true);

        RedirectView view = controller.salvar("servico", "", DOM);

        assertThat(view.getUrl(), is("/editar/api/pagina/servico/id-da-carta"));
    }

    @Test(expected = AccessDeniedException.class)
    public void usuarioNaoPodeSalvarCartaPoisNaoTemPermissao() throws Exception {
        given(userProfiles.get()).willReturn(PROFILE);
        given(userProfiles.temPermissaoParaTipoPagina(eq(TipoPermissao.EDITAR_SALVAR), any(TipoPagina.class))).willReturn(false);
        given(userProfiles.temPermissaoParaTipoPaginaOrgaoEspecifico(eq(TipoPermissao.EDITAR_SALVAR), any(TipoPagina.class), anyString())).willReturn(false);

        controller.salvar("servico", "", DOM);


    }

}