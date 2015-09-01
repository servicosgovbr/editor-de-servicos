'use strict';

module.exports = {

  controller: function (args) {
    this.descricao = args.descricao;
    this.indice = args.indice;
  },

  view: function (ctrl, args) {
    return m('.descricao', [
      m('h3.opcional', [
        'Descrição da etapa ' + (ctrl.indice + 1),
        m.component(require('tooltips').descricaoDaEtapa)
      ]),

      m.component(require('componentes/editor-markdown'), {
        rows: 3,
        oninput: m.withAttr('value', ctrl.descricao),
        value: ctrl.descricao(),
        erro: args.erro
      })
    ]);
  }
};
