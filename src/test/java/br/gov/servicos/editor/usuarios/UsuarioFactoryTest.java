package br.gov.servicos.editor.usuarios;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioFactoryTest {

    private static final String CPF = "123123123-18";
    private static final Long PAPEL_ID = 1l;
    private static final String SENHA = "senha";
    private static final String SENHA_CODIFICADA = "encoded";
    private static final String SIORG = "1234";
    private static final String SIAPE = "43214321";
    private static final String EMAIL_INST = "email@institucional.gov.br";
    private static final String EMAIL_SECUNDARIO = "email@secundario.org";
    private static final boolean SERVIDOR = true;

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
                withSenha(SENHA).
                withSiorg(SIORG).
                withSiape(SIAPE).
                withEmailInstitucional(EMAIL_INST).
                withEmailSecundario(EMAIL_SECUNDARIO).
                withServidor(SERVIDOR);
    }

    @Test
    public void criaUsuarioComCamposBasicos() {
        Usuario usuario = factory.criarUsuario(formularioUsuario);

        assertThat(usuario.getCpf(), equalTo(CPF));
        assertThat(usuario.getSiorg(), equalTo(SIORG));
        assertThat(usuario.getEmailInstitucional(), equalTo(EMAIL_INST));
        assertThat(usuario.getEmailSecundario(), equalTo(EMAIL_SECUNDARIO));
        assertThat(usuario.getSiape(), equalTo(SIAPE));
        assertThat(usuario.isServidor(), equalTo(SERVIDOR));
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

    @Test
    public void criaUsuarioComAlgunsValoresPadrão() {
        Usuario usuario = factory.criarUsuario(formularioUsuario);
        assertThat(usuario.isHabilitado(), is(true));
    }


}