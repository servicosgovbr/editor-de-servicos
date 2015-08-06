'use strict';

var Caso = require('etapas/caso');
var Casos = require('etapas/casos');
var ListaDeDocumentos = require('etapas/lista-de-documentos');

module.exports = {

  controller: function (args) {
    this.documentos = args.documentos;
    this.indice = args.indice;
  },

  view: function (ctrl) {
    return m('#' + ctrl.documentos().id, [
      m('h3', [
        'Documentação necessária para a etapa ' + (ctrl.indice + 1),
        m.component(require('tooltips').documentacao)
      ]),

      m('label.titulo', ['DOCUMENTAÇÃO NECESSÁRIA PARA TODOS OS CASOS']),

      m.component(new Caso(ListaDeDocumentos), {
        padrao: true,
        caso: ctrl.documentos().casoPadrao
      }),

      m.component(new Casos(ListaDeDocumentos), {
        casos: ctrl.documentos().outrosCasos
      })
    ]);
  }

};
