'use strict';

var Caso = require('etapas/caso');
var Casos = require('etapas/casos');
var ListaDeCustos = require('etapas/lista-de-custos');
var modelos = new require('modelos');

module.exports = {

  controller: function (args) {
    this.custos = args.custos;
    this.indice = args.indice;
  },

  view: function (ctrl, args) {
    var erros = args.erros || {};

    if (ctrl.custos() === null) {
      ctrl.custos(new modelos.Custos());
    }

    return m('#' + ctrl.custos().id, [
      m('h3', [
        'Custos da etapa ' + (ctrl.indice + 1),
        m.component(require('tooltips').custos)
      ]),

      m.component(new Caso(ListaDeCustos), {
        padrao: true,
        titulo: 'nome do custo',
        caso: ctrl.custos().casoPadrao,
        erros: erros.casoPadrao
      }),
      m.component(new Casos(ListaDeCustos), {
        titulo: 'custos para este caso',
        casos: ctrl.custos().outrosCasos,
        erros: erros.outrosCasos
      })
    ]);
  }
};
