package br.gov.servicos.editor.usuarios.cadastro;


import br.com.caelum.stella.bean.validation.CPF;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.Wither;
import org.hibernate.validator.constraints.Email;
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
public class FormularioUsuario {
    public static final String EMAIL_INVALIDO = "formato de email não valido";
    public static final String CAMPO_OBRIGATORIO = "campo obrigatório";

    @NotBlank(message = "CPF: " + CAMPO_OBRIGATORIO)
    @CPF(message = "CPF: não é valido")
    @CpfUnico
    String cpf;

    @Valid
    @ConfirmacaoSenha
    CamposSenha camposSenha = new CamposSenha();

    String papelId;

    @Servidor
    @Cidadao
    @Valid
    CamposServidor camposServidor = new CamposServidor();

    @NotBlank(message = "Orgão: " + CAMPO_OBRIGATORIO)
    String siorg;

    @NotBlank(message = "Nome Completo: " + CAMPO_OBRIGATORIO)
    String nome;

    @NotBlank(message = "Email Primário: " + CAMPO_OBRIGATORIO)
    @Email(message = "Email Primário: " + EMAIL_INVALIDO)
    String emailPrimario;

    @Email(message = "Email Secundário: " + EMAIL_INVALIDO)
    String emailSecundario;

    boolean habilitado;

    public String getSenha() {
        return this.camposSenha.getSenha();
    }
}
