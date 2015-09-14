'use strict';

var Caso = require('servico/etapas/caso');
var Casos = require('servico/etapas/casos');
var ListaDeDocumentos = require('servico/etapas/lista-de-documentos');
var modelos = require('../modelos');

module.exports = {
  view: function (ctrl, args) {
    var documentos = args.documentos;
    var indice = args.indice;

    if (documentos() === null) {
      documentos(new modelos.Documentos());
    }

    return m('#' + documentos().id, [
      m('h3', [
        'Documentação necessária para a etapa ' + (indice + 1),
        m.component(require('tooltips').documentacao)
      ]),

      m.component(new Caso(ListaDeDocumentos), {
        titulo: 'Documentação em comum para todos os casos',
        padrao: true,
        opcional: true,
        caso: documentos().casoPadrao
      }),

      m.component(new Casos(ListaDeDocumentos), {
        titulo: 'Documentação para este caso',
        casos: documentos().outrosCasos,
      })
    ]);
  }

};
