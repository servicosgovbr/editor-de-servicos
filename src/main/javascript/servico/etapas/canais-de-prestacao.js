'use strict';

var Caso = require('servico/etapas/caso');
var Casos = require('servico/etapas/casos');
var ListaDeCanaisDePrestacao = require('servico/etapas/lista-de-canais-de-prestacao');

module.exports = {
  view: function (ctrl, args) {
    var canaisDePrestacao = args.canaisDePrestacao();
    var indice = args.indice;

    return m('#' + canaisDePrestacao.id, [
      m('h3', [
        'Canais de prestacao da etapa ' + (indice + 1),
        m.component(require('tooltips').canaisDePrestacao)
      ]),

      m.component(new Caso(ListaDeCanaisDePrestacao), {
        padrao: true,
        titulo: '',
        caso: canaisDePrestacao.casoPadrao
      }),

      m.component(new Casos(ListaDeCanaisDePrestacao), {
        titulo: '',
        casos: canaisDePrestacao.outrosCasos
      })
    ]);
  }
};
