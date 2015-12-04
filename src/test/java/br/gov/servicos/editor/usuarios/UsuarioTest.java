package br.gov.servicos.editor.usuarios;

import br.gov.servicos.editor.security.GerenciadorPermissoes;
import br.gov.servicos.editor.security.Permissao;
import br.gov.servicos.editor.security.TipoPermissao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static br.gov.servicos.editor.security.TipoPermissao.DESPUBLICAR;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UsuarioTest {

    private static final String NOME_PAPEL = "EDITOR";
    private static final Permissao PUBLICAR = new Permissao(TipoPermissao.PUBLICAR.getNome());
    private static final Permissao DESPUBLICAR_ORGAO_ESPECIFICO = new Permissao(DESPUBLICAR.comOrgaoEspecifico());
    private static final String ORGAO_ID = "orgaoId";
    private static final String OUTRO_ORGAO_ID = "outroOrgaoId";

    @Mock
    private GerenciadorPermissoes gerenciadorPermissoes;
    private Usuario usuario;

    @Before
    public void setUp() {
        Usuario.setGerenciadorPermissoes(gerenciadorPermissoes);
        Papel papel = new Papel();
        papel.setNome(NOME_PAPEL);
        usuario = new Usuario().withPapel(papel).withSiorg(ORGAO_ID);
        when(gerenciadorPermissoes.getPermissoes(NOME_PAPEL)).thenReturn(
                newArrayList(PUBLICAR, DESPUBLICAR_ORGAO_ESPECIFICO));
    }

    @Test
    public void deveVerificarSeUsuarioPossuiPermissaoComOrgao() {
        assertTrue(usuario.temPermissaoComOrgao(TipoPermissao.DESPUBLICAR, ORGAO_ID));
        assertFalse(usuario.temPermissaoComOrgao(TipoPermissao.DESPUBLICAR, OUTRO_ORGAO_ID));
    }
    

}