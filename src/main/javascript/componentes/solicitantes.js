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
      m('h3', 'Quem pode utilizar este serviço?'),

      ctrl.servico().solicitantes().map(function (s, i) {
        return m('fieldset#' + s.id, {
          key: s.id
        }, [
          m('h3', 'Descrição'),
          m.component(require('componentes/editor-markdown'), {
            rows: 5,
            value: s.descricao(),
            onchange: m.withAttr('value', s.descricao)
          }),

          m('h3', 'Requisitos'),
          m.component(require('componentes/editor-markdown'), {
            rows: 5,
            value: s.requisitos(),
            onchange: m.withAttr('value', s.requisitos)
          }),

          m('button.inline.remover', {
            onclick: ctrl.remover.bind(ctrl, i)
          }, [
            m('span.fa.fa-times'), ' Remover solicitante'
          ]),

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
