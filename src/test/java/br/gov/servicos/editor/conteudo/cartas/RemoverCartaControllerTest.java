package br.gov.servicos.editor.conteudo.cartas;

import br.gov.servicos.editor.security.UserProfiles;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.function.Supplier;

import static br.gov.servicos.editor.utils.TestData.PROFILE;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RemoverCartaControllerTest {

    @Mock
    Carta carta;

    @Mock
    UserProfiles userProfiles;

    @Captor
    ArgumentCaptor<Supplier<Void>> captor;

    RemoverCartaController controller;

    @Before
    public void setUp() throws Exception {
        controller = new RemoverCartaController(userProfiles);
    }

    @Test
    public void removeCartaExistente() throws Exception {
        given(userProfiles.get()).willReturn(PROFILE);

        controller.remover(carta);

        verify(carta).remover(PROFILE);
    }
}
