package br.gov.servicos.editor.usuarios;

import br.com.caelum.stella.bean.validation.CPF;
import br.gov.servicos.editor.usuarios.cadastro.CamposSenha;
import br.gov.servicos.editor.usuarios.cadastro.ConfirmacaoSenha;
import br.gov.servicos.editor.usuarios.cadastro.CpfUnico;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@Wither
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode
public class FormularioRecuperarSenha {
    public static final String CAMPO_OBRIGATORIO = "campo obrigatório";

    @NotBlank(message = "CPF: " + CAMPO_OBRIGATORIO)
    String cpf;

    @Valid
    @ConfirmacaoSenha
    CamposSenha camposSenha = new CamposSenha();

    @NotBlank(message = "Token invalido")
    String token;

    @NotBlank(message = "Token invalido")
    String usuarioId;

}
