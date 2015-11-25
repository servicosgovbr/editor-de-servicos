package br.gov.servicos.editor.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import static java.lang.Long.valueOf;

@Component
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario findByCpf(@PathVariable("cpf") String cpf) {
        Usuario usuario = usuarioRepository.findByCpf(cpf);
        if(usuario == null) {
            throw new UsuarioInexistenteException();
        }
        return usuario;
    }

    public Iterable<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public void save(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public Usuario findById(String usuarioId) {
        return usuarioRepository.findById(valueOf(usuarioId));
    }
}
