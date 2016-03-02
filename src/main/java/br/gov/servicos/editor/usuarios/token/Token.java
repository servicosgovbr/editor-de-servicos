package br.gov.servicos.editor.usuarios.token;


import br.gov.servicos.editor.usuarios.Usuario;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Wither
@Table(name = "Tokens")
@EqualsAndHashCode
public class Token implements Serializable {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private Integer tentativasSobrando;

    public Token decrementarTentativasSobrando() {
        return this.tentativasSobrando == 0 ? this : this.withTentativasSobrando(this.tentativasSobrando - 1);
    }
}
