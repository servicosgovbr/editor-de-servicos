'use strict';

var ListaPaginas = require('home/lista-paginas/componente');

module.exports = {
  controller: function (args) {
    this.filtro = m.prop('');
    this.filtrarPublicados = m.prop();
    this.orgao = m.prop('');
  },

  view: function (ctrl) {
    moment.locale('pt-br');

    return m('#conteudo', [
      m('span.cabecalho-cor'),
      m('#wrapper', [
        m.component(require('cabecalho/cabecalho'), {
          metadados: false,
          logout: true
        }),

        m('#bem-vindo', [
          m('div.busca', [
            m('input[type=search][placeholder="Filtre os resultados por serviços, órgãos, áreas de interesse e palavras chave"]', {
              oninput: m.withAttr('value', ctrl.filtro)
            }),
            m('label', [
              m('input[type=checkbox]', {}),
              'Áreas de interesse'
            ]),

            m('label', [
              m('input[type=checkbox]', {}),
              'Órgãos'
            ]),

            m('label', [
              m('input[type=checkbox]', {}),
              'Páginas especiais'
            ]),

            m('label', [
              m('input[type=checkbox]', {}),
              'Serviços'
            ])
          ]),
          m.component(ListaPaginas, {
            filtro: {
              orgao: ctrl.orgao(),
              busca: ctrl.filtro(),
              publicados: ctrl.filtrarPublicados()
            }
          })
        ])
      ])
    ]);
  }
};
