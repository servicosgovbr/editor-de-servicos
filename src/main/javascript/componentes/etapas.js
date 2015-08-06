'use strict';

var modelos = require('modelos');

module.exports = {

  controller: function (args) {
    this.servico = args.servico;

    this.adicionar = function () {
      this.servico().etapas().push(new modelos.Etapa());
    };

    this.remover = function (i) {
      this.servico().etapas().splice(i, 1);
    };
  },

  view: function (ctrl) {
    return m('fieldset#etapas-do-servico', [
      m('h3', 'Etapas'),

      ctrl.servico().etapas().map(function (etapa, i) {
        return m('span', {
          key: etapa.id
        }, [
          m.component(require('etapas/etapa'), {
            etapa: etapa,
            gratuidade: ctrl.servico().gratuidade,
            indice: i
          }),

          m('button.remove', {
            onclick: ctrl.remover.bind(ctrl, i)
          }, [
            m('span.fa.fa-trash'),
            ' Remover etapa '
          ])
        ]);
      }),

      m('button.adicionar.adicionar-etapa', {
        onclick: ctrl.adicionar.bind(ctrl)
      }, [
        m('i.fa.fa-plus'),
        ' Adicionar etapa '
      ])
    ]);
  }
};
