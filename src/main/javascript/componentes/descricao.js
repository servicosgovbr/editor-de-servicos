'use strict';

module.exports = {

  controller: function (args) {
    this.descricao = args.descricao;
  },

  view: function (ctrl) {
    return m('fieldset#descricao', [
      m('h3', 'Descrição do serviço'),
      m.component(require('componentes/editor-markdown'), {
        rows: 10,
        oninput: m.withAttr('value', ctrl.descricao),
        value: ctrl.descricao()
      })
    ]);
  }

};
