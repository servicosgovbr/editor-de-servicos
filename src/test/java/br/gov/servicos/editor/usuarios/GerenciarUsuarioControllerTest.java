package br.gov.servicos.editor.usuarios;

import br.gov.servicos.editor.usuarios.cadastro.FormularioUsuario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GerenciarUsuarioControllerTest {

    public static final String CPF = "12312312319";
    private FormularioUsuario FORM_USUARIO = new FormularioUsuario();
    private Usuario USUARIO = new Usuario().withCpf(CPF);

    @Mock
    private UsuarioService usuarioService;

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
        verify(usuarioService).save(USUARIO);
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
        verify(usuarioService, never()).save(USUARIO);
    }

    @Test
    public void retornaMesmoUsuarioFormularioSeFormularioPossuiErros() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(factory.criarUsuario(FORM_USUARIO)).thenReturn(USUARIO);
        String endereco = controller.criar(FORM_USUARIO, bindingResult);
        assertThat(endereco, equalTo("cadastrar"));
    }

    @Test
    public void mostraInformacoesDoUsuarioNasIntrucoesDeRecuperarSenhas() {
        when(usuarioService.findByCpf(CPF)).thenReturn(USUARIO);
        ModelAndView view = controller.recuperarSenha(CPF);
        assertThat(view.getModel().get("usuario"), equalTo(USUARIO));
    }

}