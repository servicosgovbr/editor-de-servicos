package br.gov.servicos.editor.usuarios;

import br.com.caelum.stella.format.CPFFormatter;
import br.gov.servicos.editor.usuarios.cadastro.FormularioUsuario;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioFactory {
    @Autowired
    private PasswordEncoder passwordEncoder;

    private CPFFormatter cpfFormatter = new CPFFormatter();

    public Usuario criarUsuario(FormularioUsuario formulario) {
        String senhaCodificada = passwordEncoder.encode(formulario.getSenha());
        Papel papel = new Papel(Long.valueOf(formulario.getPapelId()));
        return new Usuario()
                .withCpf(cpfFormatter.unformat(formulario.getCpf()))
                .withSenha(senhaCodificada)
                .withPapel(papel)
                .withSiorg(formulario.getSiorg())
                .withSiape(StringUtils.defaultIfEmpty(formulario.getCamposServidor().getSiape(), null))
                .withEmailPrimario(formulario.getEmailPrimario())
                .withEmailSecundario(formulario.getEmailSecundario())
                .withServidor(formulario.getCamposServidor().isServidor())
                .withHabilitado(true);
    }
}
