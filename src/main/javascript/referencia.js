'use strict';

var slugify = require('slugify');
var erro = require('utils/erro-ajax');

module.exports = {

  segmentosDaSociedade: [
    'Cidadãos',
    'Empresas',
    'Órgãos e entidades públicas',
    'Demais segmentos (ONGs, organizações sociais, etc)'
  ],

  eventosDaLinhaDaVida: [
    'Apoio financeiro e crédito',
    'Aposentadoria',
    'Contas e Impostos',
    'Cuidados com a saúde',
    'Documentos e certidões',
    'Empreendedorismo e negócios',
    'Estrangeiros no Brasil',
    'Estudos',
    'Exportação de produtos e serviços',
    'Família',
    'Importação de produtos e serviços',
    'Imóveis',
    'Profissão e trabalho',
    'Veículos',
    'Viagem ao exterior'
  ],

  tiposDeCanalDePrestacao: [
    'Aplicativo móvel',
    'E-mail',
    'Fax',
    'Postal',
    'Presencial',
    'SMS',
    'Telefone',
    'Web',
    'Web: Acompanhar',
    'Web: Agendar',
    'Web: Calcular taxas',
    'Web: Consultar',
    'Web: Declarar',
    'Web: Emitir',
    'Web: Inscrever-se',
    'Web: Postos de Atendimento',
    'Web: Preencher',
    'Web: Simular'
  ].map(function (c) {
    return {
      id: slugify(c),
      text: c
    };
  }),

  descricoesDeCanaisDePrestacao: {
    'aplicativo-movel': 'Endereço para o download do aplicativo',
    'e-mail': 'Endereço de e-mail que deverá ser utilizado',
    'fax': 'Número para qual o fax deverá ser enviado',
    'postal': 'Endereço para envio de correspondência',
    'presencial': 'Endereço a visitar ',
    'sms': 'Número para qual o SMS deverá ser enviado',
    'telefone': 'Número para qual a ligação deverá realizada',
    'web': 'Endereço a visitar ',
    'web-acompanhar': 'Endereço a visitar para acompanhar um pedido esta etapa',
    'web-agendar': 'Endereço a visitar para agendar uma visita esta etapa',
    'web-calcular-taxas': 'Endereço a visitar para calcular taxas nesta etapa',
    'web-consultar': 'Endereço a visitar para consultar dados nesta etapa',
    'web-declarar': 'Endereço a visitar para fazer declarações nesta etapa',
    'web-emitir': 'Endereço a visitar para emitir documentos ou certidões nesta etapa',
    'web-inscrever-se': 'Endereço a visitar para inscrever-se nesta etapa',
    'web-postos-de-atendimento': 'Endereço a visitar para encontrar postos de atendimento que realizam esta etapa',
    'web-preencher': 'Endereço a visitar para baixar ou preencher um formulário nesta etapa',
    'web-simular': 'Endereço a visitar para simular esta etapa'
  },

  areasDeInteresse: _.once(m.request({
    method: 'GET',
    url: '/editar/api/vcge'
  }).then(function (vcge) {
    return _.sortBy(_.compact(_.map(vcge, function (v, k) {
      if (v.prefLabel) {
        return v.prefLabel[0];
      }
    })), function (area) {
      return _.deburr(area);
    });
  }, erro)),

  orgaos: _.once(m.request({
    method: 'GET',
    url: '/editar/api/orgaos'
  }).then(function (orgaos) {
    return _.sortBy((orgaos.unidades || []).map(function (o) {
      var nome = o.nome + ' (' + o.sigla + ')';
      return {
        id: slugify(nome),
        text: nome
      };
    }), function (o) {
      return o.id;
    });
  }, erro))

};
