package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.Main;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@IntegrationTest({
        "flags.importar=false",
        "flags.esquentar.cache=false",
        "server.port:0"
})
public class VCGEIntegrationTest {

    @Autowired
    VCGE vcge;

    @Test
    public void obtemEstruturaDoVCGE() throws Exception {
        assertThat(vcge.get(), allOf(is(notNullValue()), not(isEmptyString())));
    }
}
