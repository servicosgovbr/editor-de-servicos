package br.gov.servicos.editor.usuarios;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import static java.lang.Long.valueOf;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UsuarioService {

    UsuarioRepository usuarios;

    @Autowired
    public UsuarioService(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    public Usuario findByCpf(@PathVariable("cpf") String cpf) {
        Usuario usuario = usuarios.findByCpf(cpf);
        if (usuario == null) {
            throw new UsuarioInexistenteException();
        }
        return usuario;
    }

    public Iterable<Usuario> findAll() {
        return usuarios.findAll();
    }

    public Usuario save(Usuario usuario) {
        return usuarios.save(usuario);
    }

    public Usuario findById(String usuarioId) {
        return usuarios.findById(valueOf(usuarioId));
    }

    public Usuario habilitarDesabilitarUsuario(String usuarioId) {
        Usuario usuario = this.findById(usuarioId);
        return this.save(usuario.withHabilitado(!usuario.isHabilitado()));
    }

    public void desabilitarUsuario(String usuarioId) {
        Usuario usuario = this.findById(usuarioId);
        this.save(usuario.withHabilitado(false));
    }
}
