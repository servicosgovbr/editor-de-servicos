package br.gov.servicos.editor.git;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
public class MetadadosTest {

    @Test
    public void temAlteracoesSeHashPublicadoDiferenteHashEditado() {
        Metadados m = new Metadados()
                .withEditado(new Revisao()
                        .withAutor("A")
                        .withHash("asdf")
                        .withHorario(new Date()))
                .withPublicado(new Revisao()
                        .withAutor("B")
                        .withHash("fdsa")
                        .withHorario(new Date()));

        Assert.assertTrue(m.getTemAlteracoesNaoPublicadas());
    }

    @Test
    public void naoTemAlteracoesSeEditadoNaoExistir() {
        Metadados m = new Metadados()
                .withPublicado(new Revisao()
                        .withAutor("B")
                        .withHash("fdsa")
                        .withHorario(new Date()));

        Assert.assertFalse(m.getTemAlteracoesNaoPublicadas());
    }

    @Test
    public void temAlteracoesSeApenasEditadoExistir() {
        Metadados m = new Metadados()
                .withEditado(new Revisao()
                        .withAutor("B")
                        .withHash("fdsa")
                        .withHorario(new Date()));

        Assert.assertTrue(m.getTemAlteracoesNaoPublicadas());
    }

}