'use strict';

module.exports = {
  view: function () {
    return m('#conteudo', [
      m('span.cabecalho-cor'),
      m('#wrapper', [
        m.component(require('cabecalho/cabecalho'), {
          metadados: false,
          logout: true,
          nomeDaPagina: 'Erro'
        }),
        m('#erro.scroll', [
          m('h2', [
            'Acesso Negado',
            m('i.fa.fa-bomb.fa-lg')
          ]),
          m('p', 'Você não possui permissão para realizar esse tipo de operação')
        ])
      ])
    ]);
  }
};
