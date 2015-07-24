'use strict';

var Caso = require('componentes/etapa/caso');
var Casos = require('componentes/etapa/casos');
var ListaDeDocumentos = require('componentes/etapa/lista-de-documentos');

module.exports = {

  controller: function (args) {
    this.documentos = args.documentos;
  },

  view: function (ctrl) {
    return m('#' + ctrl.documentos().id, [
      m('h3', 'Documentação necessária'),

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
