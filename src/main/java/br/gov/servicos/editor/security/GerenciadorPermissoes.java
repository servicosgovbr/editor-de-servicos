package br.gov.servicos.editor.security;

import br.gov.servicos.editor.usuarios.Usuario;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class GerenciadorPermissoes implements InitializingBean {
    private Multimap<String, Permissao> map;
    private YamlPropertiesFactoryBean yamlPropertiesFactoryBean;

    @Autowired
    public GerenciadorPermissoes(YamlPropertiesFactoryBean yamlPropertiesFactoryBean) {
        this.yamlPropertiesFactoryBean = yamlPropertiesFactoryBean;
    }

    @Override
    public void afterPropertiesSet() {
        map = HashMultimap.create();
        yamlPropertiesFactoryBean.getObject().forEach((papel, permissaoObj) -> {
            String permissao = (String) permissaoObj;
            map.put(parsePapel((String) papel), new Permissao(permissao.toUpperCase()));
        });
        Usuario.setGerenciadorPermissoes(this); //necessario porque Usuario não é uma classe spring
    }

    private String parsePapel(String papel)  {
        if(!papel.matches("[\\w\\s]+\\[\\d\\]")) {
            throw new RuntimeException("Formato incorreto do arquivo de permissões. Problema com o papel: " + papel);
        }
        return papel.split("\\[")[0].toUpperCase();
    }

    public Collection<Permissao> getPermissoes(String editor) {
        return map.get(editor.toUpperCase());
    }
}
