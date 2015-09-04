package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.cartas.ListaDeConteudo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ListarConteudoControllerTest {

    ListarConteudoController controller;

    @Mock
    ListaDeConteudo listaDeConteudo;

    @Before
    public void setUp() throws Exception {
        controller = new ListarConteudoController(listaDeConteudo);
    }

    @Test
    public void deveDelegarParaListagem() throws Exception {
        controller.listar();
        verify(listaDeConteudo).listar();
    }
}