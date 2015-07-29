'use strict';

var Caso = require('componentes/etapa/caso');
var Casos = require('componentes/etapa/casos');
var ListaDeCanaisDePrestacao = require('componentes/etapa/lista-de-canais-de-prestacao');

module.exports = {

  controller: function (args) {
    this.canaisDePrestacao = args.canaisDePrestacao;
    this.indice = args.indice;
  },

  view: function (ctrl) {
    return m('#' + ctrl.canaisDePrestacao().id, [
      m('h3', 'Canais de prestacao da etapa ' + (ctrl.indice + 1)),

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
