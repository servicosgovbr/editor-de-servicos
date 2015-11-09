'use strict';

var ListaPaginas = require('home/lista-paginas/componente');

module.exports = {
  controller: function (args) {
    this.filtro = m.prop('');
    this.filtrarPublicados = m.prop();
    this.orgao = m.prop('');
    this.filtroAreasDeInteresse = m.prop('');
    this.filtroOrgaos = m.prop('');
    this.filtroPaginasEspeciais = m.prop('');
    this.filtroServicos = m.prop('');
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
              m('input[type=checkbox]', { onclick: m.withAttr('checked', ctrl.filtroAreasDeInteresse) }),
              'Páginas de áreas de interesse', m.component(require('tooltips').paginasAreasInteresse)
            ]),

            m('label', [
              m('input[type=checkbox]', { onclick: m.withAttr('checked', ctrl.filtroOrgaos) }),
              'Páginas de órgãos', m.component(require('tooltips').paginasOrgaos)
            ]),

            m('label', [
              m('input[type=checkbox]', { onclick: m.withAttr('checked', ctrl.filtroPaginasEspeciais) }),
              'Páginas temáticas', m.component(require('tooltips').paginasTematicas)
            ]),

            m('label', [
              m('input[type=checkbox]', { onclick: m.withAttr('checked', ctrl.filtroServicos) }),
              'Páginas de serviços', m.component(require('tooltips').paginasServicos)
            ])
          ]),
          m.component(ListaPaginas, {
            filtro: {
              orgao: ctrl.orgao(),
              busca: ctrl.filtro(),
              publicados: ctrl.filtrarPublicados(),
              filtroAreasDeInteresse: ctrl.filtroAreasDeInteresse(),
              filtroOrgaos: ctrl.filtroOrgaos(),
              filtroPaginasEspeciais: ctrl.filtroPaginasEspeciais(),
              filtroServicos: ctrl.filtroServicos()
            }
          })
        ])
      ])
    ]);
  }
};
