package br.gov.servicos.editor.conteudo;

import br.gov.servicos.editor.frontend.Siorg;
import br.gov.servicos.editor.git.ConteudoMetadados;

public interface ConteudoMetadadosProvider {
    ConteudoMetadados toConteudoMetadados(Siorg siorg);
}
