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
    'Web: Preencher',
    'Web: Simular'
  ].map(function (c) {
    return {
      id: slugify(c),
      text: c
    };
  }),

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
