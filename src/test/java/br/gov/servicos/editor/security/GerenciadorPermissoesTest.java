package br.gov.servicos.editor.security;

import org.junit.Test;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;


public class GerenciadorPermissoesTest {

    @Test
    public void deveTerUmMapComTodosOsPapeisPresentesNoArquivo() {
        GerenciadorPermissoes gerenciadorPermissoes = new GerenciadorPermissoes(loadProperty());
        gerenciadorPermissoes.afterPropertiesSet();
        Collection<Permissao> permissoes = gerenciadorPermissoes.getPermissoes("editor");
        assertThat(permissoes, hasSize(2));
        assertThat(permissoes, hasItem(new Permissao("editar servico")));
    }

    private YamlPropertiesFactoryBean loadProperty() {
        YamlPropertiesFactoryBean permissoes = new YamlPropertiesFactoryBean();
        permissoes.setResources(new ClassPathResource("permissoesTest.yaml"));
        permissoes.afterPropertiesSet();
        return permissoes;
    }

}