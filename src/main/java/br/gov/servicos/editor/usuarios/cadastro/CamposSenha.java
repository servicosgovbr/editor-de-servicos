package br.gov.servicos.editor.usuarios.cadastro;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;

import javax.validation.constraints.Size;

import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@Wither
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode
public class CamposSenha {
    @Size(min = 8, max = 50, message = "Senha: tamanho deve estar entre 8 e 50")
    String senha;

    String confirmacaoSenha;

    public boolean isValid() {
        return Objects.equals(senha, confirmacaoSenha);
    }
}
