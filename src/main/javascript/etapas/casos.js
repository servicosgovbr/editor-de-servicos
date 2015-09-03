'use strict';

var modelos = require('modelos');
var Caso = require('etapas/caso');

module.exports = function (componente) {

  return {
    controller: function (args) {
      this.adicionar = function (casosProp) {
        var caso = new modelos.Caso();
        caso.adicionado = true;

        var casos = casosProp();
        casos.push(caso);
        casosProp(casos);
      };

      this.remover = function (casosProp, i) {
        var casos = casosProp();
        casos.splice(i, 1);
        casosProp(casos);
      };
    },

    view: function (ctrl, args) {
      var tituloCaso = args.titulo;
      var casos = args.casos;
      var erros = args.erros || [];

      return m('.relative', [
        casos().map(function (caso, i) {
          return [
            m('label.titulo.margin-left.opcional', [
              m('strong', ['Caso ' + (i + 1)]),
              ': nome do caso',
              m.component(require('tooltips').caso)
              ]),

            m('button.remove.absolute', {
              onclick: ctrl.remover.bind(ctrl, casos, i)
            }),

            m.component(new Caso(componente), {
              titulo: tituloCaso,
              caso: m.prop(caso),
              erros: erros[i]
            })
          ];
        }),
        m('button.adicionar.adicionar-caso', {
          onclick: ctrl.adicionar.bind(ctrl, casos)
        }, [
          m('i.fa.fa-indent'),
          ' Adicionar caso '
        ])
      ]);
    }
  };
};
