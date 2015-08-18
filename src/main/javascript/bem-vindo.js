'use strict';

var slugify = require('slugify');
var erro = require('utils/erro-ajax');

module.exports = {
  controller: function (args) {
    this.filtro = m.prop('');
    this.servicos = m.prop([]);

    this.servicosFiltrados = function () {
      var servicos;

      if (_.isEmpty(_.trim(this.filtro()))) {
        servicos = this.servicos();
      } else {
        var f = new RegExp(_.trim(_.deburr(this.filtro())), 'i');
        servicos = this.servicos().filter(function (i) {
          return f.test(i.id);
        });
      }

      return _.take(servicos, 25);
    };

    this.listarServicos = _.debounce(function () {
      m.request({
          method: 'GET',
          url: '/editar/api/servicos'
        })
        .then(this.servicos, erro);
    }.bind(this), 500);

    this.excluirServico = function (id) {
      m.request({
        method: 'DELETE',
        url: '/editar/api/servico/' + slugify(id),
      }).then(this.listarServicos, erro);
    };

    this.listarServicos();
  },

  view: function (ctrl) {
    return m('#conteudo', [
      m('span.cabecalho-cor'),
      m('#wrapper', [
        m.component(require('componentes/cabecalho')),

        m('#bem-vindo', [
          m('h2', 'Bem-vindo!'),

          m('input[type=search][placeholder="Filtrar por..."]', {
            oninput: m.withAttr('value', ctrl.filtro)
          }),

          m('table', [
            m('tr', [
              m('th[width="40%"]', 'Nome'),
              m('th.center', 'Autor'),
              m('th.center', 'Última atualização'),
              m('th.right', '')
            ])
          ].concat(ctrl.servicosFiltrados().map(function (s) {
            return m('tr', [

              m('td', m('a', {
                href: '/editar/servico/' + slugify(s.id)
              }, [
                s.id.replace(/\.xml$/, '').replace(/-/g, ' ')
              ])),

              m('td.center', s.autor),

              m('td.center', moment(s.horario).fromNow()),

              m('td.right', [
                m('a', {
                  href: '/editar/servico/' + slugify(s.id)
                }, m('button', [
                  m('span.fa.fa-pencil')
                ])),
                  m('button', {
                  onclick: _.bind(ctrl.excluirServico, ctrl, s.id)
                }, [
                  m('span.fa.fa-trash-o')
                ])
              ])
            ]);
          })))
        ])
      ])
    ]);
  }
};
