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

import static java.lang.Long.valueOf;
import static lombok.AccessLevel.PRIVATE;

@Getter
@Setter
@Wither
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode
public class FormularioRecuperarSenha {

    @Valid
    @TokenCpfValido
    CamposVerificacaoRecuperarSenha camposVerificacaoRecuperarSenha = new CamposVerificacaoRecuperarSenha();

    @Valid
    @ConfirmacaoSenha
    CamposSenha camposSenha = new CamposSenha();

    public Long getUsuarioId() {
        return valueOf(camposVerificacaoRecuperarSenha.getUsuarioId());
    }

    public String getCpf() {
        return camposVerificacaoRecuperarSenha.getCpf();
    }

    public String getToken() {
        return camposVerificacaoRecuperarSenha.getToken();
    }
}
