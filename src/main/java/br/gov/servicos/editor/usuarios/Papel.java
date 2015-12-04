package br.gov.servicos.editor.usuarios;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name="PAPEIS")
public class Papel implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String nome;

    public Papel(Long id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return getNome();
    }
}
