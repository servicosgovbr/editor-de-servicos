package br.gov.servicos.editor.frontend;

import br.gov.servicos.editor.Main;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class UITest {

    WebDriver driver;

    @Value("${local.server.port}")
    int port;

    String baseUrl;

    @Before
    public void setUp() throws Exception {
        baseUrl = "http://localhost:" + port;

        driver = new HtmlUnitDriver(true);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @After
    public void tearDown() throws Exception {
        driver.close();
    }

    @Test
    public void deveSeguirSequenciaSimplesDeEdicao() throws Exception {
        driver.get(baseUrl + "/");
        assertThat(driver.getTitle(), is("Editor de Serviços - Acessar o editor de serviços"));

        driver.findElement(By.id("usuario")).sendKeys("user");
        driver.findElement(By.id("senha")).sendKeys("user");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        assertThat(driver.getTitle(), is("Editor de Serviços - Principal"));

        driver.findElement(By.id("nome")).clear();
        driver.findElement(By.id("nome")).sendKeys("Serviço 1");
        driver.findElement(By.id("nomesPopulares")).sendKeys("");
        driver.findElement(By.id("descricao")).sendKeys("O Serviço 1 facilita a ...");
        driver.findElement(By.name("solicitantes")).sendKeys("Cidadãos maiores de 18 anos");

        driver.findElement(By.id("tempoEstimado.tipo1")).click();
        driver.findElement(By.id("tempoEstimado.entreMinimo")).sendKeys("12");
        new Select(driver.findElement(By.id("tempoEstimado.entreTipoMinimo"))).selectByValue("dias úteis");

        driver.findElement(By.id("tempoEstimado.entreMaximo")).sendKeys("18");
        new Select(driver.findElement(By.id("tempoEstimado.entreTipoMaximo"))).selectByValue("dias úteis");

        driver.findElement(By.id("tempoEstimado.excecoes")).sendKeys("Para solicitantes dos tipos C, D e E, o processo pode levar mais tempo.");

        driver.findElement(By.id("gratuito2")).click();
        driver.findElement(By.id("situacao1")).click();

        driver.findElement(By.name("etapas[0].titulo")).sendKeys("Agendar atendimento presencial");
        driver.findElement(By.id("palavrasChave")).sendKeys("serviço 1");

        driver.findElement(By.id("salvar")).click();

        System.out.println(driver.getPageSource());

        assertThat(driver.getTitle(), is("Editor de Serviços - Principal"));
        assertThat(driver.findElement(By.id("nome")).getAttribute("value"), is("Serviço 1"));

        driver.findElement(By.id("nomesPopulares")).sendKeys("serviço 1, um");

        driver.findElement(By.id("salvar")).click();
        assertThat(driver.findElement(By.id("nomesPopulares")).getAttribute("value"), is("serviço 1, um"));

    }

}
