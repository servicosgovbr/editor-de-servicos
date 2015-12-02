package br.gov.servicos.editor.security;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class Permissao implements GrantedAuthority {
    private String nome;

    @Override
    public String getAuthority() {
        return nome;
    }
}
