'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;

    this.debug = function () {
      var xml = require('componentes/xml').converterParaXML(this);
      console.log(this); // jshint ignore:line
      console.log(JSON.stringify(this)); // jshint ignore:line
      console.log(xml); // jshint ignore:line
      console.log(new XMLSerializer().serializeToString(xml)); // jshint ignore:line
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
            onclick: ctrl.debug.bind(ctrl.servico)
          }, [
            m('i.fa.fa-bug')
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
