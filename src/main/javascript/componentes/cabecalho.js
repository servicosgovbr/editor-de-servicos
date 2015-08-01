'use strict';

module.exports = {

  controller: function (args) {
    this.cabecalho = (args || {}).cabecalho || {};

    this.login = m.request({
      method: 'GET',
      url: '/editar/api/usuario'
    }).then(function (data) {
      return data.username;
    });

  },

  view: function (ctrl) {
    var header = 'header';
    if (ctrl.cabecalho.erro && ctrl.cabecalho.erro()) {
      header += '.erro-conexao';
    }

    return m(header, [
        m('', m('a[href=/editar]', m('h1', 'Editor de Serviços'))),

        m.component(require('componentes/metadados'), {
          metadados: ctrl.cabecalho.metadados
        }),

        m.component(require('componentes/erro-conexao'), {
          erro: ctrl.cabecalho.erro
        }),

        m('form#logout[action=/editar/logout][method=POST]', [
          m('span', [' ', ctrl.login(), ' ']),
          m('button', [
            m('i.fa.fa-sign-out'), m.trust('&nbsp; Sair'),
          ])
        ])

    ]);
  }

};
