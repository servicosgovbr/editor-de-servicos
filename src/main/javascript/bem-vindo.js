'use strict';

var slugify = require('slugify');

module.exports = {
  controller: function (args) {
    this.filtro = m.prop('');

    this.servicos = m.request({
      method: 'GET',
      url: '/editar/api/servicos'
    }).then(function (lista) {
      return lista.map(function (i) {
        return i.replace(/\.xml$/, '').replace(/-/g, ' ');
      });
    });

    this.servicosFiltrados = function () {
      if (this.filtro() === '') {
        return [];
      }

      var f = new RegExp(this.filtro());

      return this.servicos().filter(function (i) {
        return f.test(i);
      });
    };
  },

  view: function (ctrl) {
    return m('', [
      m.component(require('componentes/cabecalho')),
      m('#principal.auto-grid', [
        m('h1', 'Bem-vindo!'),

        m('input[type=search][placeholder="Buscar"].inline-lg', {
          oninput: m.withAttr('value', ctrl.filtro)
        }),

        m('ul#servicos', [
          ctrl.servicosFiltrados().map(function (s) {
            return m('li', m('a', {
              href: '/editar/servico/' + slugify(s)
            }, s));
          })
        ])
      ])
    ]);
  }
};
