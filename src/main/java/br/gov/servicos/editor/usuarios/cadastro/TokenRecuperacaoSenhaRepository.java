package br.gov.servicos.editor.usuarios.cadastro;

import br.gov.servicos.editor.usuarios.TokenRecuperacaoSenha;
import org.springframework.data.repository.Repository;

public interface TokenRecuperacaoSenhaRepository extends Repository<TokenRecuperacaoSenha, Long> {
    void save(TokenRecuperacaoSenha tokenRecuperacaoSenha);
    TokenRecuperacaoSenha findByUsuarioId(Long usuarioId);
    void delete(Long id);
}
