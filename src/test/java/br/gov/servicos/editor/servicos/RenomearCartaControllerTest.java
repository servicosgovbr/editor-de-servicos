package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.cartas.Carta;
import br.gov.servicos.editor.oauth2.UserProfiles;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.function.Supplier;

import static br.gov.servicos.editor.utils.TestData.GOOGLE_PROFILE;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RenomearCartaControllerTest {

    @Mock
    Carta carta;

    @Mock
    UserProfiles userProfiles;

    @Captor
    ArgumentCaptor<Supplier<Void>> captor;

    RenomearCartaController controller;

    @Before
    public void setUp() throws Exception {
        controller = new RenomearCartaController(userProfiles);
    }

    @Test
    public void removeCartaExistente() throws Exception {
        given(userProfiles.get()).willReturn(GOOGLE_PROFILE);

        controller.renomear(carta, "novo-id");

        verify(carta).renomear(GOOGLE_PROFILE, "novo-id");
    }
}
