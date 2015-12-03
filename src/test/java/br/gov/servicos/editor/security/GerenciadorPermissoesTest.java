package br.gov.servicos.editor.security;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;


public class GerenciadorPermissoesTest {


    private static GerenciadorPermissoes gerenciadorPermissoes;

    @BeforeClass
    public static void setUp() {
        gerenciadorPermissoes = new GerenciadorPermissoes(loadProperty());
        gerenciadorPermissoes.afterPropertiesSet();
    }

    @Test
    public void deveTerUmMapComTodosOsPapeisPresentesNoArquivo() {
        Collection<Permissao> permissoes = gerenciadorPermissoes.getPermissoes("editor");
        assertThat(permissoes, hasSize(2));
        assertThat(permissoes, hasItem(new Permissao("EDITAR SERVICO")));
    }

    @Test
    public void deveGuardarChavesEPermissoesEmMaiuscula() {
        Collection<Permissao> permissoes = gerenciadorPermissoes.getPermissoes("EDITOR");
        assertThat(permissoes, hasSize(2));
        assertThat(permissoes, hasItem(new Permissao("EDITAR SERVICO")));
    }

    private static YamlPropertiesFactoryBean loadProperty() {
        YamlPropertiesFactoryBean permissoes = new YamlPropertiesFactoryBean();
        permissoes.setResources(new ClassPathResource("permissoesTest.yaml"));
        permissoes.afterPropertiesSet();
        return permissoes;
    }

}