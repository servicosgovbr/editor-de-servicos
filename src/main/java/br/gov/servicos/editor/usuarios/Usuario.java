package br.gov.servicos.editor.usuarios;


import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Getter
@NoArgsConstructor
@Table(name="USERS")
public class Usuario implements Serializable, UserDetails{

    @Id
    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean servidor;

    @Column(nullable = false)
    private boolean enabled;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="role_id")
    private Papel papel;

    public Usuario(String cpf, String password, Papel papel) {
        this.cpf = cpf;
        this.password = password;
        this.papel = papel;
        this.enabled = true;
        this.servidor = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Lists.newArrayList(getPapel());
    }

    @Override
    public String getUsername() {
        return cpf;
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

}
