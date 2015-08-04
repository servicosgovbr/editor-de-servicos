'use strict';

var Caso = require('etapas/caso');
var Casos = require('etapas/casos');
var ListaDeCanaisDePrestacao = require('etapas/lista-de-canais-de-prestacao');

module.exports = {

  controller: function (args) {
    this.canaisDePrestacao = args.canaisDePrestacao;
    this.indice = args.indice;
  },

  view: function (ctrl) {
    return m('#' + ctrl.canaisDePrestacao().id, [
      m('h3', [
        'Canais de prestacao da etapa ' + (ctrl.indice + 1),
        m.component(require('tooltips').canaisDePrestacao)
      ]),

      m.component(new Caso(ListaDeCanaisDePrestacao), {
        padrao: true,
        caso: ctrl.canaisDePrestacao().casoPadrao
      }),

      m.component(new Casos(ListaDeCanaisDePrestacao), {
        casos: ctrl.canaisDePrestacao().outrosCasos
      })
    ]);
  }
};
