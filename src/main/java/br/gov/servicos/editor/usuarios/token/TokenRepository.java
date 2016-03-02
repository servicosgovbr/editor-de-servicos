package br.gov.servicos.editor.usuarios.token;

import org.springframework.data.repository.Repository;

public interface TokenRepository extends Repository<Token, Long> {
    void save(Token token);

    Iterable<Token> findByUsuarioId(Long usuarioId);

    void delete(Long id);

    void deleteByUsuarioId(Long usuarioId);
}
