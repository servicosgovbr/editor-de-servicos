'use strict';

var modelos = require('modelos');
var Caso = require('componentes/etapa/caso');

module.exports = function (componente) {

  return {

    controller: function (args) {
      this.casos = args.casos;

      this.adicionar = function () {
        this.casos().push(new modelos.Caso());
      };

      this.remover = function (i) {
        this.casos().splice(i, 1);
      };
    },

    view: function (ctrl) {
      return m('', [
        ctrl.casos().map(function (caso, i) {
          return [
            m.component(new Caso(componente), {
              caso: m.prop(caso)
            }),
            m('button.inline.remover', {
              onclick: ctrl.remover.bind(ctrl, i)
            }, [
              m('span.fa.fa-times'),
              ' Remover caso '
            ])
          ];
        }),
        m('button.adicionar.adicionar-caso', {
          onclick: ctrl.adicionar.bind(ctrl)
        }, [
          m('i.fa.fa-indent'),
          ' Adicionar caso '
        ])
      ]);
    }
  };
};
