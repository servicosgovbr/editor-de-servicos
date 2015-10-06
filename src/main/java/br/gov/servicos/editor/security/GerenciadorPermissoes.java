package br.gov.servicos.editor.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static br.gov.servicos.editor.security.Permissao.ADMIN;
import static java.util.stream.Collectors.toList;

@Component
public class GerenciadorPermissoes {

    private Map<String, Permissao> permissoes;

    public GerenciadorPermissoes() {
        this.permissoes = new HashMap<>();

        this.permissoes.put("jkirchne@thoughtworks.com", ADMIN);
        this.permissoes.put("jean.kirchner@gmail.com", ADMIN);
        this.permissoes.put("ojanequi@thoughtworks.com", ADMIN);
        this.permissoes.put("srosa@thoughtworks.com", ADMIN);
        this.permissoes.put("bleite@thoughtworks.com", ADMIN);
        this.permissoes.put("gfreita@thoughtworks.com", ADMIN);
    }

    public Permissao permissao(String email) {
        return Optional.ofNullable(permissoes.get(email))
                .orElse(ADMIN);
    }

    public List<GrantedAuthority> authorities(String email) {
        return permissao(email)
                .getPermissoes()
                .stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(toList());
    }
}
