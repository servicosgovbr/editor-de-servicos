package br.gov.servicos.editor.usuarios;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioFactoryTest {

    private static final String CPF = "123123123-18";
    private static final Long PAPEL_ID = 1l;
    private static final String SENHA = "senha";
    private static final String SENHA_CODIFICADA = "encoded";

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioFactory factory;
    private FormularioUsuario formularioUsuario;

    @Before
    public void setUp() {
        formularioUsuario = new FormularioUsuario().
                withCpf(CPF).
                withPapelId(PAPEL_ID.toString()).
                withPassword(SENHA);
    }

    @Test
    public void criaUsuarioComCamposBasicos() {
        Usuario usuario = factory.criarUsuario(formularioUsuario);

        assertThat(usuario.getCpf(), equalTo(CPF));
    }

    @Test
    public void criaPasswordComPasswordEncoder() {
        when(passwordEncoder.encode(SENHA)).thenReturn(SENHA_CODIFICADA);
        Usuario usuario = factory.criarUsuario(formularioUsuario);

        assertThat(usuario.getPassword(), equalTo(SENHA_CODIFICADA));
    }

    @Test
    public void criaUsuarioComPapel() {
        Usuario usuario = factory.criarUsuario(formularioUsuario);

        assertThat(usuario.getPapel().getId(), equalTo(PAPEL_ID));
    }


}