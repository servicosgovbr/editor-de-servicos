package br.gov.servicos.editor.usuarios;


import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Wither
@Table(name="USUARIOS")
@EqualsAndHashCode
public class Usuario implements Serializable, UserDetails{

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private boolean servidor;

    @Column(nullable = false)
    private boolean habilitado;

    @Column(nullable = false)
    private String siorg;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String emailPrimario;

    @Column(unique = true)
    private String siape;

    @Column
    private String emailSecundario;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="papel_id")
    private Papel papel;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Lists.newArrayList(getPapel());
    }

    @Override
    public String getUsername() {
        return getCpf();
    }

    @Override
    public String getPassword() {
        return getSenha();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isHabilitado();
    }

}
