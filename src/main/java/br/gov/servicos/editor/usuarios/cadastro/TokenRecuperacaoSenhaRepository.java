package br.gov.servicos.editor.usuarios.cadastro;

import br.gov.servicos.editor.usuarios.TokenRecuperacaoSenha;
import org.springframework.data.repository.Repository;

public interface TokenRecuperacaoSenhaRepository extends Repository<TokenRecuperacaoSenha, String> {
    void save(TokenRecuperacaoSenha tokenRecuperacaoSenha);
}
