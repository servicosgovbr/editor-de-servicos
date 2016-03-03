package br.gov.servicos.editor.security;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;

@EqualsAndHashCode
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permissao implements GrantedAuthority {

    String nome;

    @Override
    public String getAuthority() {
        return nome;
    }
}
