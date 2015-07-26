'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    return m('fieldset#descricao', [
      m('h3', 'Descrição do serviço'),
      m.component(require('componentes/editor-markdown'), {
        rows: 10,

        oninput: function (e) {
          ctrl.servico().descricao(e.target.value);
        },

        value: ctrl.servico().descricao()
      })
    ]);
  }

};
