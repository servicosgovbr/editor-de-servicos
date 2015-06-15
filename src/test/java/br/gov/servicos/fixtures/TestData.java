package br.gov.servicos.fixtures;

import br.gov.servicos.editor.servicos.Servico;

public class TestData {

    public static final Servico SERVICO = new Servico()
            .withNome("Exemplo de serviço")
            .withDescricao("Descrição serviço");

}

