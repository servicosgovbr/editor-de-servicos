'use strict';

var Caso = require('servico/etapas/caso');
var Casos = require('servico/etapas/casos');
var ListaDeCustos = require('servico/etapas/lista-de-custos');
var modelos = require('../modelos');

module.exports = {

  view: function (ctrl, args) {
    var custos = args.custos;
    var indice = args.indice;
    var erros = args.erros || {};

    if (custos() === null) {
      custos(new modelos.Custos());
    }

    return m('#' + custos().id, [
      m('h3', [
        'Custos da etapa ' + (indice + 1),
        m.component(require('tooltips').custos)
      ]),

      m.component(new Caso(ListaDeCustos), {
        padrao: true,
        titulo: 'nome do custo',
        caso: custos().casoPadrao,
        erros: erros.casoPadrao
      }),
      m.component(new Casos(ListaDeCustos), {
        titulo: 'custos para este caso',
        casos: custos().outrosCasos,
        erros: erros.outrosCasos
      })
    ]);
  }
};
