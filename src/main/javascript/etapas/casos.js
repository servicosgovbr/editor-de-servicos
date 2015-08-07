'use strict';

var modelos = require('modelos');
var Caso = require('etapas/caso');

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
      return m('.relative', [
        ctrl.casos().map(function (caso, i) {
          return [
            m('label.titulo', ['Caso ' + (i + 1) + ': nome do caso']),

            m('button.remove.absolute', {
              onclick: ctrl.remover.bind(ctrl, i)
            }),

            m.component(new Caso(componente), {
              caso: m.prop(caso)
            })
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
