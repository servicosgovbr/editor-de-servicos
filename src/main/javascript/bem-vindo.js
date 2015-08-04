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

        m('input[type=search][placeholder="Buscar"].inline-lg', {
          oninput: m.withAttr('value', ctrl.filtro)
        }),
        m('table', [
          m('tr', [
            m('th', 'Nome'),
            m('th', 'Autor'),
            m('th', 'Horário'),
            m('th', 'Ações')
          ])
        ].concat(ctrl.servicosFiltrados().map(function (s) {
          return m('tr', [
            m('td', s.id),
            m('td', s.autor),
            m('td', moment(s.horario).fromNow()),
            m('td', m('a', {
              href: '/editar/servico/' + slugify(s.id)
            }, 'editar'))
          ]);
        })))
      ])
    ]);
  }
};
