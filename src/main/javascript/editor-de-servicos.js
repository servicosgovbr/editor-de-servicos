'use strict';

var modelos = require('modelos');
var importarXml = require('componentes/importar-xml');
var exportarXml = require('componentes/exportar-xml');
var salvarXml = require('componentes/salvar-xml');

module.exports = {

  controller: function () {
    this.servico = m.prop(new modelos.Servico());

    this.salvar = function () {
      return salvarXml(this.servico().nome(), exportarXml(this.servico()))
        .then(importarXml)
        .then(this.servico);
    };
  },

  view: function (ctrl) {
    var binding = {
      servico: ctrl.servico
    };

    return m('', [
      m.component(require('componentes/cabecalho'), {
        salvar: ctrl.salvar.bind(ctrl)
      }),

      m.component(require('componentes/menu-lateral'), binding),

      m('#principal.auto-grid', [
        m.component(require('componentes/dados-basicos'), binding),
        m.component(require('componentes/solicitantes'), binding),
        m.component(require('componentes/etapas'), binding),
        m.component(require('componentes/dados-complementares'), binding),
      ])
    ]);
  }

};
