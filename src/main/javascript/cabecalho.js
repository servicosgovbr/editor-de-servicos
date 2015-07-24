'use strict';

module.exports = {

  controller: function () {
    this.login = m.request({
      method: 'GET',
      url: '/editar/api/usuario'
    }).then(function (data) {
      return data.username;
    });
  },

  view: function (ctrl) {
    return m('header', [
      m('.auto-grid', [
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
