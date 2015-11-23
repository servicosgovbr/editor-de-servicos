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
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Wither
@Table(name="Tokens")
@EqualsAndHashCode
public class TokenRecuperacaoSenha implements Serializable {

    @Id
    @Column(nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Date dataCriacao;

}
