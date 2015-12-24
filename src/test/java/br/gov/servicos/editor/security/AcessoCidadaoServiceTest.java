package br.gov.servicos.editor.security;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

import br.gov.servicos.editor.usuarios.Papel;
import br.gov.servicos.editor.usuarios.PapelRepository;
import br.gov.servicos.editor.usuarios.Usuario;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;


@RunWith(MockitoJUnitRunner.class)
public class AcessoCidadaoServiceTest {


    public static final String CIDADAO = "CIDADAO";

    @Mock
    PapelRepository repository;

    @Mock
    GerenciadorPermissoes gerenciadorPermissoes;

    AcessoCidadaoService service;

    @Before
    public void setUp() {
        Mockito.when(repository.findByNome(CIDADAO)).thenReturn(criaPapelCidadao());
        Mockito.when(gerenciadorPermissoes.getPermissoes(anyString())).thenReturn(Collections.emptyList());

        service = new AcessoCidadaoService(repository);
        Usuario.setGerenciadorPermissoes(gerenciadorPermissoes);
    }



    @Test
    public void deveBuscarPapelCidadao() {
        chamaServico();
        verify(repository).findByNome(CIDADAO);
    }

    @Test
    public void deveAutorizarAcesso() {
        chamaServico();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        assertThat(principal, instanceOf(Usuario.class));
        assertThat(authentication.isAuthenticated(), is(true));
    }

    @Test
    public void usuarioAutorizadoDeveSerCidadao() {
        chamaServico();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = (Usuario) authentication.getPrincipal();


        assertThat(usuario.getPapel().getNome(), is(CIDADAO));
    }

    private void chamaServico() {
        service.autenticaCidadao("Nome", "um@email.com", "123.123.123-12");
    }

    private Papel criaPapelCidadao() {
        Papel cidadao = new Papel();
        cidadao.setNome(CIDADAO);
        return cidadao;
    }
}
