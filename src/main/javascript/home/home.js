'use strict';

var ListaPaginas = require('home/lista-paginas/componente');

module.exports = {
  controller: function (args) {
    this.filtro = m.prop('');
    this.filtrarPublicados = m.prop();
    this.orgao = m.prop('');
    this.filtroOrgaos = m.prop('');
    this.filtroPaginasTematicas = m.prop('');
    this.filtroServicos = m.prop('');
  },

  view: function (ctrl) {
    moment.locale('pt-br');

    return m('#conteudo', [
      m('span.cabecalho-cor'),
      m('#wrapper', [
        m.component(require('cabecalho/cabecalho'), {
          metadados: false,
          nomeDaPagina: 'Lista de páginas',
          logout: true
        }),

        m('#bem-vindo', [
          m('div.busca', [
            m('input[type=search][placeholder="Filtre os resultados por serviços, órgãos e palavras chave"]', {
              oninput: m.withAttr('value', ctrl.filtro)
            }),
            m('label', [
              m('input[type=checkbox]', {
                onclick: m.withAttr('checked', ctrl.filtroOrgaos)
              }),
              'Páginas de órgãos', m.component(require('tooltips').paginasOrgaos)
            ]),

            m('label', [
              m('input[type=checkbox]', {
                onclick: m.withAttr('checked', ctrl.filtroPaginasTematicas)
              }),
              'Páginas temáticas', m.component(require('tooltips').paginasTematicas)
            ]),

            m('label', [
              m('input[type=checkbox]', {
                onclick: m.withAttr('checked', ctrl.filtroServicos)
              }),
              'Páginas de serviços', m.component(require('tooltips').paginasServicos)
            ])
          ]),
          m.component(ListaPaginas, {
            filtro: {
              orgao: ctrl.orgao(),
              busca: ctrl.filtro(),
              publicados: ctrl.filtrarPublicados(),
              filtroOrgaos: ctrl.filtroOrgaos(),
              filtroPaginasTematicas: ctrl.filtroPaginasTematicas(),
              filtroServicos: ctrl.filtroServicos()
            }
          })
        ])
      ])
    ]);
  }
};
