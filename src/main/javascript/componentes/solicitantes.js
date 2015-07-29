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
        return m('fieldset#' + s.id, {
          key: s.id
        }, [
          m('h3', 'Tipo de solicitante'),

          m.component(require('componentes/editor-markdown'), {
            rows: 2,
            value: s.tipo(),
            onchange: m.withAttr('value', s.tipo)
          }),

          m('h3', 'Requisitos que tornam o solicitante elegível'),

          m.component(require('componentes/editor-markdown'), {
            rows: 2,
            value: s.requisitos(),
            onchange: m.withAttr('value', s.requisitos)
          }),

          m('button.inline.remover', {
            onclick: ctrl.remover.bind(ctrl, i)
          }, m('span.fa.fa-trash-o'))
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
