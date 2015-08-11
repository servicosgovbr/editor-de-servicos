'use strict';

var t = function (txt) {
  return {
    view: function () {
      return m('span.tooltip.dica-campo', [
        m('span', [txt])
      ]);
    }
  };
};

module.exports = {
  nome: t('Este é o nome que aparecerá em destaque. Deve ser curto e claro para o solicitante.'),
  sigla: t('Se o nome deste serviço possuir uma sigla, inclua-a neste campo. Exemplo: CNH.'),
  nomesPopulares: t('Caso este serviço seja conhecido por nomes populares, liste-os nos campos abaixo.'),
  descricao: t('Faça uma descrição sintética do serviço, contendo o resultado que será entregue ao solicitante.'),
  tempoTotalEstimado: t('Descreva quanto tempo, em média, o solicitante levará para obter este serviço.'),
  gratuidade: t('Escolha a opção sobre a gratuidade deste serviço para o solicitante.'),
  solicitantes: t('Descreva quem pode utilizar este serviço e quais requisitos devem ser cumpridos.'),
  tituloDaEtapa: t('Escreva um título que indica de forma simples o objetivo dessa etapa.'),
  descricaoDaEtapa: t('Descreva quais atividades contemplam esta etapa do serviço.'),
  documentacao: t('Descreva documentos, certidões ou declarações necessárias para esta etapa.'),
  custos: t('Informe a estimativa mais fiel possível com relação aos custos ou taxas sobre esta etapa.'),
  canaisDePrestacao: t('Informe os canais de prestação disponíveis para realizar esta etapa.'),
  orgaoResponsavel: t('Selecione o órgão responsável por este serviço e que responde pela ouvidoria.'),
  segmentosDaSociedade: t('Escolha um ou mais segmentos da sociedade que acessam este serviço.'),
  eventosDaLinhaDaVida: t('Escolha um ou mais eventos da linha da vida relacionados a este serviço.'),
  areasDeInteresse: t('Escolha uma ou mais áreas de interesse (VCGE) relacionadas a este serviço.'),
  palavrasChave: t('Escreva pelo menos três palavras-chave relacionadas a este serviço.'),
  legislacoes: t('Insira o link da legislação a partir do site LexML (www.lexml.gov.br).'),
};
