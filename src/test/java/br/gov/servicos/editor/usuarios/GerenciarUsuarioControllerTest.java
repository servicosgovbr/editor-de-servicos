package br.gov.servicos.editor.usuarios;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GerenciarUsuarioControllerTest {

    private FormularioUsuario FORM_USUARIO = new FormularioUsuario().withCpf("12312312319");
    private Usuario USUARIO = new Usuario();

    @Mock
    private UsuarioRepository repository;

    @Mock
    private UsuarioFactory factory;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private GerenciarUsuarioController controller;

    @Test
    public void salvaNovoUsuario() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(factory.criarUsuario(FORM_USUARIO)).thenReturn(USUARIO);
        controller.criar(FORM_USUARIO, bindingResult);
        verify(repository).save(USUARIO);
    }

    @Test
    public void retornarNovoUsuarioFormularioCasoCadastroTenhaSidoFeito() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(factory.criarUsuario(FORM_USUARIO)).thenReturn(USUARIO);
        String endereco = controller.criar(FORM_USUARIO, bindingResult);
        assertThat(endereco, equalTo("redirect:/editar/usuarios/usuario?sucesso"));

    }


    @Test
    public void naoSalvaSeFormularioPossuiErros() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(factory.criarUsuario(FORM_USUARIO)).thenReturn(USUARIO);
        controller.criar(FORM_USUARIO, bindingResult);
        verify(repository, never()).save(USUARIO);
    }

    @Test
    public void retornaMesmoUsuarioFormularioSeFormularioPossuiErros() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(factory.criarUsuario(FORM_USUARIO)).thenReturn(USUARIO);
        String endereco = controller.criar(FORM_USUARIO, bindingResult);
        assertThat(endereco, equalTo("cadastrar"));
    }

}