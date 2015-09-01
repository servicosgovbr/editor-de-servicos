'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    var validador = ctrl.servico().validador;

    return m('fieldset#descricao', [
      m('h3', [
        'Descrição do serviço',
        m.component(require('tooltips').descricao)
      ]),

      m.component(require('componentes/editor-markdown'), {
        rows: 6,

        oninput: function (e) {
          ctrl.servico().descricao(e.target.value);
        },

        value: ctrl.servico().descricao(),
        erro: function () { return validador.hasError('descricao'); }
      })
    ]);
  }
};
