'use strict';

var ultimoErro = require('utils/ultimo-erro');

module.exports = {
  view: function (ctrl) {
    var mensagem = ultimoErro.get() || 'A aplicação detectou uma falha inesperada. Por favor, tente a operação novamente.';

    return m('#conteudo', [
      m('span.cabecalho-cor'),
      m('#wrapper', [
        m.component(require('cabecalho/cabecalho'), {
          metadados: false,
          logout: true
        }),

        m('#erro', [
          m('h2', ['Ocorreu um erro ', m('i.fa.fa-bomb.fa-lg')]),
          m('p', mensagem)
        ])
      ])
    ]);
  }
};
