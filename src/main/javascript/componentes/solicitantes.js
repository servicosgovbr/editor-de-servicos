'use strict';

var modelos = require('modelos');

module.exports = {

  controller: function (args) {
    this.servico = args.servico;

    this.adicionar = function () {
      this.servico().solicitantes().push(new modelos.Solicitante());
      this.adicionado = true;
    };

    this.remover = function (i) {
      this.servico().solicitantes().splice(i, 1);
    };
  },

  view: function (ctrl) {
    return m('fieldset#solicitantes', [
      m('h2', 'solicitantes'),
      m('h3', [
        'Quem pode utilizar este serviço?',
        m.component(require('tooltips').solicitantes)
      ]),

      ctrl.servico().solicitantes().map(function (s, i) {
        return m.component(require('componentes/solicitante'), {
          solicitante: s,
          index: i,
          showDelete: ctrl.servico().solicitantes().length > 1,
          remover: ctrl.remover.bind(ctrl, i)
        });
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
