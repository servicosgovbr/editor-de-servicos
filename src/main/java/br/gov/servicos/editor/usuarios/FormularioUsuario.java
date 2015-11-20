package br.gov.servicos.editor.usuarios;


import br.com.caelum.stella.bean.validation.CPF;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@Wither
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode
public class FormularioUsuario {
    public static final String EMAIL_INVALIDO = "formato de email não valido";
    public static final String CAMPO_OBRIGATORIO = "campo obrigatório";

    @NotBlank(message = "CPF: " + CAMPO_OBRIGATORIO)
    @CPF(message = "CPF: não é valido")
    @CpfUnico
    String cpf;

    @Size(min=8, max=50, message = "Senha: tamanho deve estar entre 8 e 50")
    String senha;

    String papelId;

    @Servidor
    @Cidadao
    @Valid
    CamposServidor camposServidor = new CamposServidor();

    String siorg;

    @NotBlank(message = "Email Primario: " + CAMPO_OBRIGATORIO)
    @Email(message = "Email Primario: " + EMAIL_INVALIDO)
    String emailPrimario;

    @Email(message = "Email Secundario: " + EMAIL_INVALIDO)
    String emailSecundario;
}
