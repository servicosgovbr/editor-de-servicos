'use strict';

var modelos = require('modelos');
var Caso = require('etapas/caso');

module.exports = function (componente) {

  return {

    controller: function (args) {
      this.tituloCaso = args.titulo;
      this.casos = args.casos;

      this.adicionar = function () {
        var caso = new modelos.Caso();
        caso.adicionado = true;
        this.casos().push(caso);
      };

      this.remover = function (i) {
        this.casos().splice(i, 1);
      };
    },

    view: function (ctrl) {
      return m('.relative', [
        ctrl.casos().map(function (caso, i) {
          return [
            m('label.titulo.margin-left.opcional', [
              m('strong', ['Caso ' + (i + 1)]),
              ': nome do caso'
              ]),

            m('button.remove.absolute', {
              onclick: ctrl.remover.bind(ctrl, i)
            }),

            m.component(new Caso(componente), {
              titulo: ctrl.tituloCaso,
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
