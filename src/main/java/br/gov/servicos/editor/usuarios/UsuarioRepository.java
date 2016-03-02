package br.gov.servicos.editor.usuarios;


import org.springframework.data.repository.Repository;

public interface UsuarioRepository extends Repository<Usuario, Long> {
    Usuario findByCpf(String cpf);

    Usuario save(Usuario usuario);

    Usuario findBySiape(String value);

    Iterable<Usuario> findAll();

    Usuario findById(Long usuarioId);
}
