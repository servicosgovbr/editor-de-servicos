'use strict';

var modelos = require('modelos');

module.exports = {

  controller: function (args) {
    this.servico = args.servico;

    this.adicionar = function () {
      this.servico().solicitantes().push(new modelos.Solicitante());
    };

    this.remover = function (i) {
      this.servico().solicitantes().splice(i, 1);
    };
  },

  view: function (ctrl) {
    return m('fieldset#solicitantes', [
      m('h3', [
        'Quem pode utilizar este serviço?',
        m.component(require('tooltips').solicitantes)
      ]),

      ctrl.servico().solicitantes().map(function (s, i) {
        return m('fieldset#' + s.id + '.relative', {
          key: s.id
        }, [
          m('h3', 'Tipo de solicitante'),

           m('button.remove.absolute', {
            onclick: ctrl.remover.bind(ctrl, i)
          }),

          m.component(require('componentes/editor-markdown'), {
            rows: 3,
            value: s.tipo(),
            onchange: m.withAttr('value', s.tipo)
          }),

          m('h3.opcional', 'Requisitos que tornam o solicitante elegível'),

          m.component(require('componentes/editor-markdown'), {
            rows: 5,
            value: s.requisitos(),
            onchange: m.withAttr('value', s.requisitos)
          })
        ]);
      }),

      m('button.adicionar.adicionar-solicitante', {
        onclick: ctrl.adicionar.bind(ctrl)
      }, [
        m('i.fa.fa-plus'),
        ' Adicionar solicitante '
      ])
    ]);
  }
};
