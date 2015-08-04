'use strict';

var slugify = require('slugify');

module.exports = {
  controller: function (args) {
    this.filtro = m.prop('');

    this.servicos = m.request({
      method: 'GET',
      url: '/editar/api/servicos'
    });

    this.servicosFiltrados = function () {
      if (this.filtro() === '') {
        return [];
      }

      var f = new RegExp(this.filtro());

      return this.servicos().filter(function (i) {
        return f.test(i.id);
      });
    };
  },

  view: function (ctrl) {
    return m('#wrapper', [
      m.component(require('componentes/cabecalho')),

      m('#bem-vindo', [
        m('h2', 'Bem-vindo!'),

        m('input[type=search][placeholder="Buscar"]', {
          oninput: m.withAttr('value', ctrl.filtro)
        }),

        m('table', [
          m('tr', [
            m('th[width="40%"]', 'Nome'),
            m('th.center', 'Autor'),
            m('th.center', 'Última atualização'),
            m('th.right', 'Ações')
          ])
        ].concat(ctrl.servicosFiltrados().map(function (s) {
          return m('tr', [
            m('td', s.id.replace(/\.xml$/, '').replace(/-/g, ' ')),
            m('td.center', s.autor),
            m('td.center', moment(s.horario).fromNow()),
            m('td.right', m('a', {
              href: '/editar/servico/' + slugify(s.id)
            }, [
              m('span.fa.fa-pencil'),
              m.trust('&nbsp; Editar')
            ]))
          ]);
        })))
      ])
    ]);
  }
};
