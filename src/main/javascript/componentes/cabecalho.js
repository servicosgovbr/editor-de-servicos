'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;

    this.salvar = function () {
      var xml = require('componentes/xml').converterParaXML(this.servico);

      m.request({
          method: 'POST',
          url: '/editar/v3/servico',
          data: xml,
          config: function (xhr) {
            xhr.setRequestHeader('Accepts', 'application/xml');
            xhr.setRequestHeader('Content-Type', 'application/xml');
          },
          serialize: function (svc) {
            return new XMLSerializer().serializeToString(svc);
          },
          deserialize: function (data) {
            return data;
          }
        })
        .then(function (resultado) {
          console.log(resultado); // jshint ignore:line
        });
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
