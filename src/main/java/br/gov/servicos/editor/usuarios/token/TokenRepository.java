package br.gov.servicos.editor.usuarios.token;

import br.gov.servicos.editor.usuarios.token.Token;
import org.springframework.data.repository.Repository;

public interface TokenRepository extends Repository<Token, Long> {
    void save(Token token);
    Iterable<Token> findByUsuarioIdOrderByDataCriacaoAsc(Long usuarioId);
    void delete(Long id);
}
