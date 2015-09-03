package br.gov.servicos.editor.servicos;

import br.gov.servicos.editor.cartas.ListaDeCartas;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ListarCartasControllerTest {

    ListarCartasController controller;

    @Mock
    ListaDeCartas listaDeCartas;

    @Before
    public void setUp() throws Exception {
        controller = new ListarCartasController(listaDeCartas);
    }

    @Test
    public void deveDelegarParaListagem() throws Exception {
        controller.listar();
        verify(listaDeCartas).listar();
    }
}