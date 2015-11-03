'use strict';

var slugify = require('slugify');
var erro = require('utils/erro-ajax');

var tiposDePaginaList = [
  'Área de Interesse',
  'Página Especial',
  'Órgão',
  'Serviço'
].map(function (t) {
  return {
    id: slugify(t),
    text: t
  };
});

var tiposDePaginaMap = _.indexBy(tiposDePaginaList, 'id');

module.exports = {

  segmentosDaSociedade: [
    'Cidadãos',
    'Empresas',
    'Órgãos e entidades públicas',
    'Demais segmentos (ONGs, organizações sociais, etc)'
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

  tiposDeCanalDePrestacaoVisualizar: {
    'aplicativo-movel': {
      text: 'Aplicativo móvel',
      destacado: false,
      descricaoLink: ''
    },
    'e-mail': {
      text: 'E-mail',
      destacado: false,
      descricaoLink: ''
    },
    'fax': {
      text: 'Fax',
      destacado: false,
      descricaoLink: ''
    },
    'postal': {
      text: 'Postal',
      destacado: false,
      descricaoLink: ''
    },
    'presencial': {
      text: 'Presencial',
      destacado: false,
      descricaoLink: ''
    },
    'sms': {
      text: 'SMS',
      destacado: false,
      descricaoLink: ''
    },
    'telefone': {
      text: 'Telefone',
      destacado: false,
      descricaoLink: ''
    },
    'web': {
      text: 'Web',
      destacado: true,
      descricaoLink: 'Acesse o site'
    },
    'web-acompanhar': {
      text: 'Web',
      destacado: true,
      descricaoLink: 'Acompanhe esta etapa do serviço'
    },
    'web-agendar': {
      text: 'Web',
      destacado: true,
      descricaoLink: 'Agende esta etapa do serviço'
    },
    'web-calcular-taxas': {
      text: 'Web',
      destacado: true,
      descricaoLink: 'Calcule as taxas desta etapa'
    },
    'web-consultar': {
      text: 'Web',
      destacado: true,
      descricaoLink: 'Realize uma consulta nesta etapa'
    },
    'web-declarar': {
      text: 'Web',
      destacado: true,
      descricaoLink: 'Realize uma declaração nesta etapa'
    },
    'web-emitir': {
      text: 'Web',
      destacado: true,
      descricaoLink: 'Faça a emissão para esta etapa'
    },
    'web-inscrever-se': {
      text: 'Web',
      destacado: true,
      descricaoLink: 'Inscreva-se'
    },
    'web-postos-de-atendimento': {
      text: 'Web',
      destacado: true,
      descricaoLink: 'Conheça os postos de atendimento'
    },
    'web-preencher': {
      text: 'Web',
      destacado: true,
      descricaoLink: 'Preencha esta etapa do serviço'
    },
    'web-simular': {
      text: 'Web',
      destacado: true,
      descricaoLink: 'Simule esta etapa do serviço'
    }
  },

  unidadesDeTempoVisualizar: {
    'minutos': 'minutos',
    'horas': 'horas',
    'dias-corridos': 'dias corridos',
    'dias-uteis': 'dias úteis',
    'meses': 'meses'
  },

  orgaosFemininos: [
      'comissao',
      'defensoria',
      'empresa',
      'fundacao',
      'receita',
      'receital',
      'secretaria'
  ],

  descricoesDeCanaisDePrestacao: {
    'aplicativo-movel': 'Endereço na web para o download do aplicativo',
    'e-mail': 'Endereço de e-mail que deverá ser utilizado',
    'fax': 'Número para qual o fax deverá ser enviado',
    'postal': 'Endereço para envio de correspondência',
    'presencial': 'Endereço que deverá ser visitado',
    'sms': 'Número para qual o SMS deverá ser enviado',
    'telefone': 'Número para qual a ligação deverá ser realizada',
    'web': 'Endereço na web que deverá ser visitado (preencha o endereço com http://)',
    'web-acompanhar': 'Endereço na web para acompanhar um pedido para esta etapa (preencha o endereço com http://)',
    'web-agendar': 'Endereço na web para agendar uma visita nesta etapa (preencha o endereço com http://)',
    'web-calcular-taxas': 'Endereço na web para calcular taxas para esta etapa (preencha o endereço com http://)',
    'web-consultar': 'Endereço na web para consultar dados para esta etapa (preencha o endereço com http://)',
    'web-declarar': 'Endereço na web para fazer declarações para esta etapa (preencha o endereço com http://)',
    'web-emitir': 'Endereço na web para emitir documentos ou certidões para esta etapa (preencha o endereço com http://)',
    'web-inscrever-se': 'Endereço na web para inscrever-se nesta etapa (preencha o endereço com http://)',
    'web-postos-de-atendimento': 'Endereço na web para encontrar postos de atendimento que realizam esta etapa (preencha o endereço com http://)',
    'web-preencher': 'Endereço na web baixar ou preencher um formulário para esta etapa (preencha o endereço com http://)',
    'web-simular': 'Endereço na web para simular esta etapa (preencha o endereço com http://)'
  },

  unidadesDeTempo: [
    'minutos',
    'horas',
    'dias corridos',
    'dias úteis',
    'meses'
  ].map(function (t) {
    return {
      id: slugify(t),
      text: t
    };
  }),

  areasDeInteresse: _.once(m.request({
    method: 'GET',
    url: '/editar/api/vcge'
  }).then(function (vcge) {
    return _.sortBy(_.filter(_.compact(_.map(vcge, function (v, k) {
      if (v.prefLabel) {
        return v.prefLabel[0];
      }
    })), function (area) {
      return area.indexOf('Outros em') < 0;
    }), function (area) {
      return _.deburr(area);
    });
  }, erro)),

  tiposDePagina: tiposDePaginaList,

  tipoDePagina: function (id) {
    var tipo = tiposDePaginaMap[id];

    if (!tipo) {
      throw new Error('Tipo de página não encontrado para id: ' + id);
    }

    return tipo.text;
  }
};
