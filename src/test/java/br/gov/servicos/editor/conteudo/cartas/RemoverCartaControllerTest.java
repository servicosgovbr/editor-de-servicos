package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.conteudo.ConteudoVersionado;
import br.gov.servicos.editor.conteudo.ConteudoVersionadoFactory;
import br.gov.servicos.editor.security.UserProfiles;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.function.Supplier;

import static br.gov.servicos.editor.conteudo.TipoPagina.SERVICO;
import static br.gov.servicos.editor.utils.TestData.PROFILE;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RemoverCartaControllerTest {

    @Mock
    ConteudoVersionado carta;

    @Mock
    UserProfiles userProfiles;

    @Mock
    ConteudoVersionadoFactory factory;

    @Captor
    ArgumentCaptor<Supplier<Void>> captor;

    RemoverCartaController controller;

    @Before
    public void setUp() throws Exception {
        given(factory.pagina(anyString(), eq(SERVICO)))
                .willReturn(carta);
        controller = new RemoverCartaController(userProfiles, factory);
    }

    @Test
    public void removeCartaExistente() throws Exception {
        given(userProfiles.get())
                .willReturn(PROFILE);

        controller.remover("");
        verify(carta).remover(PROFILE);
    }

}
