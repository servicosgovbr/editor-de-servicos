'use strict';

var carregar = require('xml/carregar-servico');
var exportar = require('xml/exportar');
var salvar = require('xml/salvar');
var modelos = require('modelos');

var cabecalho = new modelos.Cabecalho();

var converter = function (item) {
  return carregar(item.id, cabecalho).then(function (s) {
    return salvar(item.id, exportar(s), cabecalho.metadados);
  }).then(function (x) {
    console.log(x); //jshint ignore:line
    return x;
  }).then(m.redraw);
};

var conversor = {

  controller: function (args) {
    this.convertidos = [];

    this.converterTodos = function () {
      m.request({
        url: '/editar/api/servicos',
        background: true
      }).then(function (servicos) {
        return servicos.map(_.bind(function (servico) {
          converter(servico);
          this.convertidos.push(servico.id);
        }, this));
      });
    };
  },

  view: function (ctrl) {
    return m('', [
      m('button', {
        onclick: _.bind(_.once(ctrl.converterTodos), ctrl)
      }, 'Converter'),

      m('h1', 'Convertendo...'),

      m('ul', ctrl.convertidos.map(function (x) {
        return m('li', x);
      }))
    ]);
  }
};

module.exports = function () {
  m.route.mode = 'pathname';

  m.route(document.body, '/editar', {
    '/editar': require('bem-vindo'),
    '/editar/erro': require('erro'),
    '/editar/converter': conversor,
    '/editar/servico/:id': require('editor-de-servicos')
  });
};
