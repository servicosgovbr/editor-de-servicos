'use strict';

var EditorBase = require('componentes/editor-base');
var CabecalhoModel = require('cabecalho/cabecalho-model');
var input = require('componentes/input');
var servicoEmEdicao = require('servico/servico-em-edicao');

var importarXml = require('xml/importar-xml');

module.exports = {
  controller: function() {
    this.cabecalho = new CabecalhoModel();
    this.url = m.prop('');

    this.ok = function() {
      importarXml(this.url())
        .then(servicoEmEdicao.manter)
        .then(function() {
          m.route('/editar/servico/novo');
        });
    };
  },

  view: function (ctrl) {
    return m.component(EditorBase, {
      config: _.noop,
      cabecalhoConfig: {
        metadados: false,
        logout: true,
        cabecalho: ctrl.cabecalho
      },
      componentes: [
        m('#importar-xml', {
          method: 'POST',
          action: '/editar/importar-xml'
        }, [
          m('h3', 'Informe a URL de um XML de Serviço:'),
          m.component(input, {
            prop: ctrl.url
          }),
          m('button', {
            onclick: _.bind(ctrl.ok, ctrl)
          }, 'Importar')
        ])
      ]
    });
  }
};
