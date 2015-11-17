package br.gov.servicos.editor.conteudo.cartas;

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
public class RenomearCartaControllerTest {

    @Mock
    Carta carta;

    @Mock
    UserProfiles userProfiles;

    @Mock
    ConteudoVersionadoFactory factory;

    @Captor
    ArgumentCaptor<Supplier<Void>> captor;

    RenomearCartaController controller;

    @Before
    public void setUp() throws Exception {
        controller = new RenomearCartaController(userProfiles, factory);
        given(factory.pagina(anyString(), eq(SERVICO)))
                .willReturn(carta);
    }

    @Test
    public void removeCartaExistente() throws Exception {
        given(userProfiles.get()).willReturn(PROFILE);
        controller.renomear("", "novo-id");
        verify(carta).renomear(PROFILE, "novo-id");
    }
}
