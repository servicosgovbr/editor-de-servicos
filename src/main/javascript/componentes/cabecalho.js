'use strict';

var importarXml = require('componentes/importar-xml');
var exportarXml = require('componentes/exportar-xml');
var salvarXml = require('componentes/salvar-xml');

module.exports = {

  controller: function (args) {
    this.servico = args.servico;

    this.salvar = function () {
      return salvarXml(exportarXml(this.servico)).then(importarXml);
    };

    this.login = m.request({
      method: 'GET',
      url: '/editar/api/usuario'
    }).then(function (data) {
      return data.username;
    });

  },

  view: function (ctrl) {
    return m('header', {
      style: {
        height: '0px'
      }
    }, [
      m('', [
        m('.ferramentas', [
          m('button.inline.debug', {
            onclick: ctrl.salvar.bind(ctrl)
          }, [
            m('i.fa.fa-save')
          ]),
        ]),

        m('.titulo', m('h1', 'Editor de Serviços')),

        m('#logout', [
          m('span', [' ', ctrl.login(), ' ']),
          m('button', [
            m('i.fa.fa-sign-out'), m.trust('&nbsp; Sair'),
          ])
        ])
      ]),
    ]);
  }

};
