'use strict';

module.exports = {

  controller: function (args) {
    this.metadados = (args || {}).metadados;

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
        m('.titulo', m('a[href=/editar]', m('h1', 'Editor de Serviços'))),

        m.component(require('componentes/metadados'), {
          metadados: ctrl.metadados
        }),

        m('form#logout[action=/editar/logout][method=POST]', [
          m('span', [' ', ctrl.login(), ' ']),
          m('button', [
            m('i.fa.fa-sign-out'), m.trust('&nbsp; Sair'),
          ])
        ])
      ]),
    ]);
  }

};
