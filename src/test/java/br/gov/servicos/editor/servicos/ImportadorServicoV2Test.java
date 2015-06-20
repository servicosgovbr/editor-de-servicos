package br.gov.servicos.editor.servicos;

import com.google.common.io.Files;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Paths;

import static br.gov.servicos.fixtures.TestData.SERVICO_V2;
import static java.nio.charset.Charset.defaultCharset;
import static java.util.Optional.of;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ImportadorServicoV2Test {

    @Mock(answer = Answers.RETURNS_SMART_NULLS)
    Cartas cartas;

    String caminhoRepositorio;

    @Before
    public void setUp() throws Exception {
        caminhoRepositorio = new ClassPathResource("repositorio-cartas-servico").getFile().getAbsolutePath();
    }

    @Test
    public void deveImportarServicoNoFormatoV2() throws Exception {
        String id = "exemplo-servico-v2";

        given(cartas.conteudoServicoV2(id))
                .willReturn(of(Files.toString(
                        Paths.get(caminhoRepositorio, "cartas-servico", "v2", "servicos", id + ".xml").toFile(),
                        defaultCharset())));

        Servico servico = new ImportadorServicoV2(cartas).carregar(id).get();

        assertThat(servico, samePropertyValuesAs(SERVICO_V2));
    }
}