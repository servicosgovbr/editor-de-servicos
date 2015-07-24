'use strict';

module.exports = {

  controller: function (args) {
    this.descricao = args.descricao;
  },

  view: function (ctrl) {
    return m('.descricao', [
      m('h3', 'Descrição da etapa'),
      m.component(require('componentes/editor-markdown'), {
        rows: 5,
        oninput: m.withAttr('value', ctrl.descricao),
        value: ctrl.descricao()
      })
    ]);
  }
};
