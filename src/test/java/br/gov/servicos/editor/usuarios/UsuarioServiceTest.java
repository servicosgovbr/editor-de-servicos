package br.gov.servicos.editor.usuarios;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioServiceTest {

    public static final String CPF = "12312312312";
    private static final boolean HABILITADO = Boolean.TRUE;
    private static final Long USUARIO_ID = new Long(12345);
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
}