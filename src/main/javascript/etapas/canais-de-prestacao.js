'use strict';

var Caso = require('etapas/caso');
var Casos = require('etapas/casos');
var ListaDeCanaisDePrestacao = require('etapas/lista-de-canais-de-prestacao');

module.exports = {

  controller: function (args) {
    this.canaisDePrestacao = args.canaisDePrestacao;
    this.indice = args.indice;
  },

  view: function (ctrl, args) {
    var erros = args.erros || {};

    return m('#' + ctrl.canaisDePrestacao().id, [
      m('h3', [
        'Canais de prestacao da etapa ' + (ctrl.indice + 1),
        m.component(require('tooltips').canaisDePrestacao)
      ]),

      m.component(new Caso(ListaDeCanaisDePrestacao), {
        padrao: true,
        titulo: '',
        caso: ctrl.canaisDePrestacao().casoPadrao,
        erros: erros.casoPadrao
      }),

      m.component(new Casos(ListaDeCanaisDePrestacao), {
        titulo: '',
        casos: ctrl.canaisDePrestacao().outrosCasos,
        erros: erros.outrosCasos
      })
    ]);
  }
};
