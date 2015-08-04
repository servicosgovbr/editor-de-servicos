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
        caso: ctrl.custos().casoPadrao
      }),
      m.component(new Casos(ListaDeCustos), {
        casos: ctrl.custos().outrosCasos
      })
    ]);
  }
};
