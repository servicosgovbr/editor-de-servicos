package br.gov.servicos.editor.usuarios;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioServiceTest {

    public static final String CPF = "12312312312";
    private static final boolean HABILITADO = Boolean.TRUE;
    private static final Long USUARIO_ID = 12345L;
    private static final Usuario USUARIO = new Usuario().withCpf(CPF).withHabilitado(HABILITADO);

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    @Test
    public void deveRetornarUsuarioSeEleExiste() {
        when(repository.findByCpf(CPF)).thenReturn(USUARIO);
        assertThat(service.findByCpf(CPF), equalTo(USUARIO));
    }

    @Test(expected = UsuarioInexistenteException.class)
    public void lancaExcecaoCasoUsuarioNaoExisteNasIntrucoesDeRecuperarSenha() {
        when(repository.findByCpf(CPF)).thenReturn(null);
        service.findByCpf(CPF);
    }

    @Test
    public void inverteValorCampoHabilitado() {
        when(repository.findById(USUARIO_ID)).thenReturn(USUARIO);
        when(repository.save(USUARIO)).thenReturn(USUARIO);
//        assertThat(service.habilitarDesabilitarUsuario(USUARIO_ID.toString()), equalTo(USUARIO));
    }

    @Test
    public void desabilitaUsuario() {
        when(repository.findById(USUARIO_ID)).thenReturn(USUARIO.withHabilitado(true));
        service.desabilitarUsuario(USUARIO_ID.toString());
        verify(repository).save(USUARIO.withHabilitado(false));
    }
}