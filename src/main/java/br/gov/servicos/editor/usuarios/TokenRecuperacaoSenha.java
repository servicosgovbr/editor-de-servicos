package br.gov.servicos.editor.usuarios;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

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

    @Column
    private LocalDateTime dataCriacao;

}
