'use strict';

var importarXml = require('componentes/importar-xml-v3');
var exportarXml = require('componentes/exportar-xml');
var salvarXml = require('componentes/salvar-xml');
var slugify = require('slugify');
var carregarServico = require('carregar-servico');

module.exports = {

  controller: function () {
    this.servico = carregarServico(
      m.route.param('versao'),
      m.route.param('id')
    );

    this.salvar = function () {
      return salvarXml(slugify(this.servico().nome()), exportarXml(this.servico()))
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
