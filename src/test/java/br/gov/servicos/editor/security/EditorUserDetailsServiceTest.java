package br.gov.servicos.editor.security;

import br.gov.servicos.editor.usuarios.UsuarioRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EditorUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private EditorUserDetailsService service;

    @Test
    public void desformatarCpfAntesDeProcurar() {
        String cpfFormatado = "123.123.123-12";
        service.loadUserByUsername(cpfFormatado);
        Mockito.verify(usuarioRepository).findByCpf("12312312312");
    }

}