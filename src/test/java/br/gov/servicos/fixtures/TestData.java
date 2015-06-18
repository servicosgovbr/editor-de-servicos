package br.gov.servicos.fixtures;

import br.gov.servicos.editor.servicos.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class TestData {

    public static final Servico SERVICO_V1 = new Servico()
            .withNome("Passaporte")
            .withDescricao("Execução de atividades relacionadas ao serviço de expedição e controle de documentos de viagem, emitidos pela Polícia Federal: • Passaporte comum; • Passaporte para estrangeiro; • Laissez-Passer; • Passaporte de emergência Como Obter o Passaporte: • Preencher o formulário disponível no portal da Polícia Federal . Em caso de dúvidas sobre o preenchimento, informações podem ser obtidas pelo telefone 194. • Pagar a taxa devida para a emissão do documento através da Guia de Recolhimento da União – GRU, na rede bancária, respeitando sua data de vencimento. • Comparecer pessoalmente nos locais de atendimento da Polícia Federal munido da documentação original exigida, da GRU paga e do protocolo da solicitação, para fornecer os dados biométricos (fotografia, impressões digitais e assinatura). • O passaporte será entregue pessoalmente a seu titular, mediante apresentação de documento de identificação. Observação: A GRU será gerada automaticamente após o preenchimento do formulário, sendo imprescindível preencher o número do CPF do requerente ou do seu responsável, se for o caso. Para Passaporte de Emergência: 1. Preencher o formulário disponível no portal da Polícia Federal. Em caso de dúvidas sobre o preenchimento, informações podem ser obtidas pelo telefone 194; 2. Comparecer pessoalmente nos locais de atendimento da Polícia Federal autorizados a emitir Passaporte de Emergência (verificar no portal), munido da documentação exigida; 3. Aguardar o deferimento da solicitação de passaporte, uma vez que será avaliada a situação emergencial; 4. Se deferido, pagar a taxa devida para a emissão do documento através da Guia de Recolhimento da União – GRU –, na rede bancária, respeitando sua data de vencimento. Observação: - Na cidade de São Paulo/SP, o Passaporte de Emergência apenas é emitido no posto localizado no prédio da Superintendência Regional, na Lapa, e na cidade do Rio de Janeiro/RJ, apenas no posto localizado no Aeroporto Internacional Tom Jobim (Galeão).\n" +
                    "\n" +
                    "* [Informações gerais sobre Passaporte](http://www.dpf.gov.br/servicos/passaporte)\n" +
                    "* [Dúvidas sobre Emissão de Passaportes: ligue 194]()\n" +
                    "* [Unidades do Departamento da Polícia Federal de cada região](http://www.dpf.gov.br/institucional/pf-pelo-brasil/)\n" +
                    "* [Documentação necessária para requerer Passaporte](http://www.dpf.gov.br/servicos/passaporte/documentacao-necessaria)\n")
            .withEtapas(singletonList(new Etapa()
                    .withTitulo("Acesso ao serviço")
                    .withCustos(singletonList(new Custo().withValor("R$ 156,07")))
                    .withCanaisDePrestacao(asList(
                            new CanalDePrestacao().withTipo("Web").withDescricao("http://www.dpf.gov.br/servicos/passaporte/requerer-passaporte").withPreferencial(true),
                            new CanalDePrestacao().withTipo("Agendamento").withDescricao("")
                    ))))
            .withOrgao(new Orgao().withId("ministerio-da-justica-mj").withNome("Ministério da Justiça (MJ)"))
            .withAreasDeInteresse(singletonList("Transportes"))
            .withEventosDaLinhaDaVida(asList("Viagem ao exterior", "Documentos e certidões"))
            .withSegmentosDaSociedade(singletonList("Cidadãos"));

    public static final Servico SERVICO_V2 = new Servico()
            .withNome("Carteira Nacional de Habilitação (CNH)")
            .withNomesPopulares("carta de motorista, carteira, carta, cnh, habilitação")
            .withDescricao("A CNH blah blah blah...")
            .withPalavrasChave("carta de motorista, carteira, carta, cnh, habilitação")
            .withAreasDeInteresse(asList("Comércio e Serviços", "Comunicações"))
            .withSegmentosDaSociedade(asList("Cidadãos", "Empresas"))
            .withEventosDaLinhaDaVida(asList("Documentos e certidões", "Contas e Impostos"))
            .withLegislacoes(asList(
                    "http://www.lexml.gov.br/urn/urn:lex:br:federal:decreto:2009-08-11;6932",
                    "http://www.lexml.gov.br/urn/urn:lex:br:federal:lei:1993-06-21;8666"))
            .withSolicitantes(singletonList("Cidadãos maiores de 18 anos"))
            .withTempoEstimado(new TempoEstimado()
                            .withTipo("entre")
                            .withEntreMinimo("15")
                            .withEntreTipoMinimo("dias")
                            .withEntreMaximo("20")
                            .withEntreTipoMaximo("dias")
                            .withExcecoes("Para solicitantes dos tipos C, D e E, o processo pode levar mais tempo.")
            )
            .withEtapas(singletonList(new Etapa()
                            .withTitulo("Agendar prova teórica do CFC")
                            .withDescricao("A prova teórica do CFC deve ser agendada nas...")
                            .withDocumentos(asList("RG", "CPF", "Termo de conclusão de curso no CFC"))
                            .withCustos(singletonList(new Custo().withDescricao("Taxa de emissão").withValor("78,50")))
                            .withCanaisDePrestacao(asList(
                                    new CanalDePrestacao().withDescricao("Posto de atendimento").withPreferencial(true).withTipo("Presencial"),
                                    new CanalDePrestacao().withDescricao("555").withPreferencial(false).withTipo("Telefone")
                            ))
            ))
            .withOrgao(new Orgao().withId("ministerio-da-saude-ms").withNome("Ministério da Saúde"))
            .withGratuito(true)
            .withSituacao("Sim: serviço parcialmente eletrônico (partes presenciais, via telefone ou em papel)");
}

