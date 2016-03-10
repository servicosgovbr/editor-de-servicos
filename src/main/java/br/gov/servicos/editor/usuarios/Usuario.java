package br.gov.servicos.editor.usuarios;


import br.gov.servicos.editor.security.GerenciadorPermissoes;
import br.gov.servicos.editor.security.TipoPermissao;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Wither
@Table(name = "USUARIOS")
@EqualsAndHashCode
public class Usuario implements UserDetails {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    @JoinColumn(name = "papel_id")
    private Papel papel;

    private static GerenciadorPermissoes gerenciadorPermissoes;

    public static void setGerenciadorPermissoes(GerenciadorPermissoes gerenciadorPermissoes) {
        Usuario.gerenciadorPermissoes = gerenciadorPermissoes;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = newArrayList(getPapel());
        authorities.addAll(gerenciadorPermissoes.getPermissoes(getPapel().getNome()));
        return authorities;
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

    public boolean temPermissaoComOrgao(TipoPermissao permissao, String orgaoId) {
        return getAuthorities().stream().anyMatch(grantedAuthority ->
                permissaoIgual(permissao, grantedAuthority) ||
                        permissaoIgualComOrgao(permissao, grantedAuthority, orgaoId));
    }

    private boolean permissaoIgualComOrgao(TipoPermissao permissao, GrantedAuthority authority, String orgaoId) {
        return authority.getAuthority().equals(permissao.comOrgaoEspecifico()) && getSiorg().equals(orgaoId);
    }

    private boolean permissaoIgual(TipoPermissao permissao, GrantedAuthority authority) {
        return authority.getAuthority().equals(permissao.getNome());
    }

    public boolean temPermissao(String permissao) {
        return getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(permissao));
    }
}
