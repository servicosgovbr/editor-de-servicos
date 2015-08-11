'use strict';

var modelos = require('modelos');
var importarXml = require('xml/importar-v3');
var exportarXml = require('xml/exportar');
var salvarXml = require('xml/salvar');
var slugify = require('slugify');
var carregarServico = require('xml/carregar-servico');

module.exports = {
  controller: function () {
    this.cabecalho = new modelos.Cabecalho();
    this.servico = carregarServico(m.route.param('id'), this.cabecalho);

    this.salvar = function () {
      var erro = require('utils/erro-ajax');

      return salvarXml(slugify(this.servico().nome()), exportarXml(this.servico()), this.cabecalho.metadados)
        .then(importarXml, erro)
        .then(this.servico, erro)
        .then(_.bind(this.cabecalho.limparErro, this.cabecalho), erro);
    };
  },

  view: function (ctrl) {
    var binding = {
      servico: ctrl.servico
    };

    return m('#conteudo', [
      m('span.cabecalho-cor'),
      m('#wrapper', [
        m.component(require('componentes/cabecalho'), {
          salvar: _.bind(ctrl.salvar, ctrl),
          cabecalho: ctrl.cabecalho
        }),
        m.component(require('componentes/menu-lateral'), binding),

        m('#servico', m('.scroll', [
          m.component(require('componentes/dados-basicos'), binding),
          m.component(require('componentes/solicitantes'), binding),
          m.component(require('componentes/etapas'), binding),
          m.component(require('componentes/outras-informacoes'), binding),
        ]))
      ])
    ]);
  }
};
