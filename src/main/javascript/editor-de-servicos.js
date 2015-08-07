'use strict';

var modelos = require('modelos');
var importarXml = require('xml/importar-v3');
var exportarXml = require('xml/exportar');
var salvarXml = require('xml/salvar');
var slugify = require('slugify');
var carregarServico = require('carregar-servico');

module.exports = {
  controller: function () {
    this.cabecalho = new modelos.Cabecalho();
    this.servico = carregarServico(m.route.param('id'), this.cabecalho);

    this.deveSalvar = m.prop(false);

    this.salvar = function () {
      if (!this.deveSalvar()) {
        console.log('Não modificado'); // jshint ignore:line
        return;
      }

      var erro = _.bind(function () {
        this.cabecalho.tentarNovamente(_.bind(this.salvar, this));
      }, this);

      return salvarXml(slugify(this.servico().nome()), exportarXml(this.servico()), this.cabecalho.metadados)
        .then(importarXml, erro)
        .then(this.servico, erro)
        .then(_.bind(this.cabecalho.limparErro, this.cabecalho), erro)
        .then(_.bind(function () {
          this.deveSalvar(false);
        }, this));
    };

    window.setInterval(_.bind(this.salvar, this), 10000);
  },

  view: function (ctrl) {
    var binding = {
      servico: ctrl.servico
    };

    var deveSalvarAutomaticamente = _.bind(function () {
      this.deveSalvar(true);
    }, ctrl);

    return m('#conteudo', [
      m('span.cabecalho-cor'),
      m('#wrapper', [
        m.component(require('componentes/cabecalho'), {
          cabecalho: ctrl.cabecalho
        }),
        m.component(require('componentes/menu-lateral'), binding),

        m('#servico', {
          onchange: deveSalvarAutomaticamente,
          onclick: _.wrap(deveSalvarAutomaticamente, function (fn, e) {
            var target = jQuery(e.target);

            if (target.is('button') || target.parents('button').size() > 0) {
              return fn(e);
            }
          })
        }, m('.scroll', [
          m.component(require('componentes/dados-basicos'), binding),
          m.component(require('componentes/solicitantes'), binding),
          m.component(require('componentes/etapas'), binding),
          m.component(require('componentes/outras-informacoes'), binding),
        ]))
      ])
    ]);
  }
};
