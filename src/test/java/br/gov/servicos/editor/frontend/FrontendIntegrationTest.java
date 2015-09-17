package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.Main;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.net.HttpURLConnection;
import java.net.URL;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@IntegrationTest({"server.port:0"})
public class FrontendIntegrationTest {

    @Value("${local.server.port}")
    int port;

    @Test
    public void abrePaginaInfoComSucesso() throws Exception {
        HttpURLConnection home = (HttpURLConnection) new URL(format("http://localhost:%d/", port)).openConnection();
        assertThat(home.getResponseCode(), is(302));

        HttpURLConnection info = (HttpURLConnection) new URL(format("http://localhost:%d/editar/info", port)).openConnection();
        assertThat(info.getResponseCode(), is(200));

        HttpURLConnection health = (HttpURLConnection) new URL(format("http://localhost:%d/editar/health", port)).openConnection();
        assertThat(health.getResponseCode(), is(200));
    }

}