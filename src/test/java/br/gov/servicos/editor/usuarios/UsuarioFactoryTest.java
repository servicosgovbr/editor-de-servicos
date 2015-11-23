package br.gov.servicos.editor.usuarios;

import br.gov.servicos.editor.usuarios.cadastro.CamposSenha;
import br.gov.servicos.editor.usuarios.cadastro.CamposServidor;
import br.gov.servicos.editor.usuarios.cadastro.FormularioUsuario;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioFactoryTest {

    private static final String CPF = "12312312318";
    private static final Long PAPEL_ID = 1l;
    private static final String SENHA = "senha";
    private static final String SENHA_CODIFICADA = "encoded";
    private static final String SIORG = "1234";
    private static final String SIAPE = "43214321";
    private static final String EMAIL_PRIMARIO = "email@institucional.gov.br";
    private static final String EMAIL_SECUNDARIO = "email@secundario.org";
    private static final boolean SERVIDOR = true;
    private static final String NOME = "Nome completo";

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
                withCamposSenha(new CamposSenha().withSenha(SENHA)).
                withSiorg(SIORG).
                withCamposServidor(new CamposServidor().withServidor(SERVIDOR).withSiape(SIAPE)).
                withEmailPrimario(EMAIL_PRIMARIO).
                withEmailSecundario(EMAIL_SECUNDARIO).
                withNome(NOME);
    }

    @Test
    public void criaUsuarioComCamposBasicos() {
        Usuario usuario = factory.criarUsuario(formularioUsuario);

        assertThat(usuario.getCpf(), equalTo(CPF));
        assertThat(usuario.getSiorg(), equalTo(SIORG));
        assertThat(usuario.getEmailPrimario(), equalTo(EMAIL_PRIMARIO));
        assertThat(usuario.getEmailSecundario(), equalTo(EMAIL_SECUNDARIO));
        assertThat(usuario.getSiape(), equalTo(SIAPE));
        assertThat(usuario.isServidor(), equalTo(SERVIDOR));
        assertThat(usuario.getNome(), equalTo(NOME));
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

    @Test
    public void criaUsuarioComSiapeNullSeValorForVazio() {
        CamposServidor camposSerividorSemSiape = formularioUsuario.getCamposServidor().withSiape("");
        Usuario usuario = factory.criarUsuario(formularioUsuario.withCamposServidor(camposSerividorSemSiape));
        assertNull(usuario.getSiape());
    }

    @Test
    public void desformataCpf() {
        String cpfFormatado = "123.123.123-12";
        Usuario usuario = factory.criarUsuario(formularioUsuario.withCpf(cpfFormatado));
        assertThat(usuario.getCpf(), equalTo("12312312312"));
    }


}