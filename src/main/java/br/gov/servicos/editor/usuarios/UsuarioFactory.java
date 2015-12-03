package br.gov.servicos.editor.usuarios;

import br.com.caelum.stella.format.CPFFormatter;
import br.gov.servicos.editor.usuarios.cadastro.CamposServidor;
import br.gov.servicos.editor.usuarios.cadastro.FormularioUsuario;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class UsuarioFactory {
    private CPFFormatter cpfFormatter = new CPFFormatter();

    public Usuario criarUsuario(FormularioUsuario formulario) {
        Papel papel = new Papel(Long.valueOf(formulario.getPapelId()));
        return popularUsuarioApartirDoFormulario(new Usuario(), formulario).withPapel(papel).withHabilitado(true);
    }

    public Usuario atualizaUsuario(Usuario usuario, FormularioUsuario formulario) {
        return popularUsuarioApartirDoFormulario(usuario, formulario);
    }

    private Usuario popularUsuarioApartirDoFormulario(Usuario usuario, FormularioUsuario formulario){
        return usuario
                .withCpf(cpfFormatter.unformat(formulario.getCpf()))
                .withSiorg(formulario.getSiorg())
                .withSiape(StringUtils.defaultIfEmpty(formulario.getCamposServidor().getSiape(), null))
                .withEmailPrimario(formulario.getEmailPrimario())
                .withEmailSecundario(formulario.getEmailSecundario())
                .withServidor(formulario.getCamposServidor().isServidor())
                .withNome(formulario.getNome());
    }

    public FormularioUsuario criaFormulario(Usuario usuario) {
        return new FormularioUsuario()
                .withCpf(cpfFormatter.format(usuario.getCpf()))
                .withPapelId(usuario.getPapel() != null ? String.valueOf(usuario.getPapel().getId()) : null)
                .withSiorg(usuario.getSiorg())
                .withCamposServidor(new CamposServidor().withSiape(usuario.getSiape()))
                .withEmailPrimario(usuario.getEmailPrimario())
                .withEmailSecundario(usuario.getEmailSecundario())
                .withHabilitado(usuario.isHabilitado())
                .withNome(usuario.getNome());
    }
}
