'use strict';

module.exports = {
  view: function (ctrl) {
    return m('#conteudo', [
      m('span.cabecalho-cor'),
      m('#wrapper', [
        m.component(require('componentes/cabecalho')),

        m('#erro', [
          m('h2', ['Ocorreu um erro ', m('i.fa.fa-bomb.fa-lg')]),

          m('p', 'A aplicação detectou uma falha inesperada. Por favor, tente a operação novamente.')
        ])
      ])
    ]);
  }
};
