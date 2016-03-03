package br.gov.servicos.editor.security;

import br.gov.servicos.editor.usuarios.Usuario;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GerenciadorPermissoes implements InitializingBean {

    @NonFinal
    Multimap<String, Permissao> map;

    YamlPropertiesFactoryBean properties;

    @Autowired
    public GerenciadorPermissoes(YamlPropertiesFactoryBean properties) {
        this.properties = properties;
    }

    @Override
    public void afterPropertiesSet() {
        map = HashMultimap.create();
        properties.getObject().forEach((papel, permissao) -> {
            map.put(parsePapel(papel), new Permissao(permissao.toString().toUpperCase()));
        });
        Usuario.setGerenciadorPermissoes(this); // necessário porque Usuario não é uma classe Spring
    }

    private String parsePapel(Object papelObj) {
        String papel = (String) papelObj;
        if (!papel.matches("[\\w\\s]+\\[\\d+\\]")) {
            throw new RuntimeException("Formato incorreto do arquivo de permissões. Problema com o papel: " + papel);
        }
        return papel.split("\\[")[0].toUpperCase();
    }

    public Collection<Permissao> getPermissoes(String editor) {
        return map.get(editor.toUpperCase());
    }
}
