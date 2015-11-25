package br.gov.servicos.editor.usuarios;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;
import org.hibernate.validator.constraints.NotBlank;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@Wither
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode

public class CamposVerificacaoRecuperarSenha {
    public static final String CAMPO_OBRIGATORIO = "campo obrigatório";

    @NotBlank(message = "CPF: " + CAMPO_OBRIGATORIO)
    String cpf;

    @NotBlank(message = "Token invalido")
    String token;

    @NotBlank(message = "Token invalido")
    String usuarioId;

}
