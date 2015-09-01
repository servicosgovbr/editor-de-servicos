'use strict';

var Caso = require('etapas/caso');
var Casos = require('etapas/casos');
var ListaDeDocumentos = require('etapas/lista-de-documentos');
var modelos = new require('modelos');

module.exports = {

  controller: function (args) {
    this.documentos = args.documentos;
    this.indice = args.indice;

  },

  view: function (ctrl) {
    if (ctrl.documentos() === null) {
      ctrl.documentos(new modelos.Documentos());
    }

    return m('#' + ctrl.documentos().id, [
      m('h3', [
        'Documentação necessária para a etapa ' + (ctrl.indice + 1),
        m.component(require('tooltips').documentacao)
      ]),

      m.component(new Caso(ListaDeDocumentos), {
        padrao: true,
        titulo: 'Documentação em comum para todos os casos',
        opcional: true,
        caso: ctrl.documentos().casoPadrao
      }),

      m.component(new Casos(ListaDeDocumentos), {
        titulo: 'Documentação para este caso',
        casos: ctrl.documentos().outrosCasos
      })
    ]);
  }

};
