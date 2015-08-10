'use strict';

var Caso = require('etapas/caso');
var Casos = require('etapas/casos');
var ListaDeCustos = require('etapas/lista-de-custos');

module.exports = {

  controller: function (args) {
    this.custos = args.custos;
    this.indice = args.indice;
  },

  view: function (ctrl) {
    return m('#' + ctrl.custos().id, [
      m('h3', [
        'Custos da etapa ' + (ctrl.indice + 1),
        m.component(require('tooltips').custos)
      ]),

      m.component(new Caso(ListaDeCustos), {
        padrao: true,
        titulo: 'nome do custo',
        caso: ctrl.custos().casoPadrao
      }),
      m.component(new Casos(ListaDeCustos), {
        titulo: 'custos para este caso',
        casos: ctrl.custos().outrosCasos
      })
    ]);
  }
};
