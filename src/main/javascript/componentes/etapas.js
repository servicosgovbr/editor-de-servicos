'use strict';

var modelos = require('modelos');

module.exports = {

  controller: function (args) {
    this.etapas = args.etapas;
    this.gratuidade = args.gratuidade;

    this.adicionar = function () {
      this.etapas.push(new modelos.Etapa());
    };

    this.remover = function (i) {
      this.etapas.splice(i, 1);
    };
  },

  view: function (ctrl) {
    return m('fieldset#etapas', [
      m('h3', 'Etapas'),

      ctrl.etapas.map(function (etapa, i) {
        return m('span', {
          key: etapa.id
        }, [
          m.component(require('componentes/etapa/etapa'), {
            etapa: etapa,
            gratuidade: ctrl.gratuidade
          }),

          m('button.inline.remover', {
            onclick: ctrl.remover.bind(ctrl, i)
          }, [
            m('span.fa.fa-times'),
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
