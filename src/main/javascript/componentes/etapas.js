'use strict';

var modelos = require('modelos');

module.exports = {

  controller: function (args) {
    this.servico = args.servico;

    this.adicionar = function () {
      this.servico().etapas().push(new modelos.Etapa());
    };
  },

  view: function (ctrl) {
    return m('fieldset#etapas-do-servico', [
      m('h2', 'ETAPAS DO SERVIÇO'),

      ctrl.servico().etapas().map(function (etapa, i) {
        return m('span', {
          key: etapa.id
        }, [
          m.component(require('etapas/etapa'), {
            etapa: etapa,
            gratuidade: ctrl.servico().gratuidade,
            indice: i
          })
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
