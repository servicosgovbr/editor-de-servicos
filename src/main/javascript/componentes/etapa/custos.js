'use strict';

var Caso = require('componentes/etapa/caso');
var Casos = require('componentes/etapa/casos');
var ListaDeCustos = require('componentes/etapa/lista-de-custos');

module.exports = {

  controller: function (args) {
    this.custos = args.custos;
    this.indice = args.indice;
  },

  view: function (ctrl) {
    return m('#' + ctrl.custos().id, [
      m('h3', 'Custos da etapa ' + (ctrl.indice + 1)),

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
