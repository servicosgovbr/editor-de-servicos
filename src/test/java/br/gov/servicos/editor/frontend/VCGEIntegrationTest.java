package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.conteudo.cartas.RepositorioGitIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class VCGEIntegrationTest extends RepositorioGitIntegrationTest {

    @Autowired
    VCGE vcge;

    @Before
    public void setup() {
        setupBase().build();
    }

    @Test
    public void obtemEstruturaDoVCGE() throws Exception {
        assertThat(vcge.get(), allOf(is(notNullValue()), not(isEmptyString())));
    }
}
