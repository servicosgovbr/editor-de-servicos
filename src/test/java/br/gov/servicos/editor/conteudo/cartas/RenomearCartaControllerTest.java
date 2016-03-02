package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.ConteudoVersionadoFactory;
import br.gov.servicos.editor.conteudo.TipoPagina;
import br.gov.servicos.editor.security.TipoPermissao;
import br.gov.servicos.editor.security.UserProfiles;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.access.AccessDeniedException;

import static br.gov.servicos.editor.conteudo.TipoPagina.SERVICO;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;


@RunWith(MockitoJUnitRunner.class)
public class RenomearCartaControllerTest {

    @Mock
    ConteudoVersionado carta;

    @Mock
    ConteudoVersionadoFactory factory;

    @Mock
    UserProfiles userProfiles;


    @Test(expected = ConteudoInexistenteException.class)
    public void deveLancarConteudoInexistenteSeConteudoNaoExiste() throws ConteudoInexistenteException {
        RenomearCartaController controller = new RenomearCartaController(userProfiles, factory);
        given(factory.pagina(anyString(), any()))
                .willReturn(carta);

        given(userProfiles.temPermissaoParaTipoPaginaOrgaoEspecifico(eq(TipoPermissao.CRIAR), eq(TipoPagina.SERVICO), anyString())).willReturn(true);
        given(carta.getTipo()).willReturn(SERVICO);
        given(carta.existe()).willReturn(false);

        controller.renomear("id", "novoNome");
    }

    @Test(expected = AccessDeniedException.class)
    public void retornarAcessoNegadoCasoUsuarioNaoTenhaAcesso() throws ConteudoInexistenteException {
        RenomearCartaController controller = new RenomearCartaController(userProfiles, factory);
        given(userProfiles.temPermissaoParaTipoPagina(eq(TipoPermissao.CRIAR), eq(TipoPagina.SERVICO))).willReturn(false);
        given(userProfiles.temPermissaoParaTipoPaginaOrgaoEspecifico(eq(TipoPermissao.CRIAR), eq(TipoPagina.SERVICO), anyString())).willReturn(false);
        given(factory.pagina(anyString(), any()))
                .willReturn(carta);
        given(carta.existe()).willReturn(true);

        controller.renomear("id", "novoNome");
    }

}