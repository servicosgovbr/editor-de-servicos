package br.gov.servicos.editor.usuarios;


import org.springframework.data.repository.Repository;

public interface UsuarioRepository extends Repository<Usuario, String> {
    Usuario findByCpf(String cpf);
    Usuario save(Usuario usuario);
    boolean exists(String cpf);
}
