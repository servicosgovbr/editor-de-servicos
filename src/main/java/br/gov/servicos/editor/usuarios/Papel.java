package br.gov.servicos.editor.usuarios;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name="ROLES")
public class Papel implements GrantedAuthority {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    public Papel(Long id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return getName();
    }
}
