'use strict';

module.exports = {

  controller: function (args) {
    this.salvar = args.salvar;

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
