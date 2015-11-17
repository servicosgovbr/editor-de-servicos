package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.conteudo.cartas.RepositorioGitIntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import java.net.HttpURLConnection;
import java.net.URL;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FrontendIntegrationTest extends RepositorioGitIntegrationTest {

    @Value("${local.server.port}")
    int port;

    @Before
    public void setup() {
        setupBase().build();
    }

    @Test
    public void abrePaginaInfoComSucesso() throws Exception {
        HttpURLConnection info = (HttpURLConnection) new URL(format("http://localhost:%d/editar/info", port)).openConnection();
        assertThat(info.getResponseCode(), is(200));

        HttpURLConnection health = (HttpURLConnection) new URL(format("http://localhost:%d/editar/health", port)).openConnection();
        assertThat(health.getResponseCode(), is(200));
    }

}