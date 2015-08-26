'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
    this.validador = this.servico().validador;
  },

  view: function (ctrl) {
    return m('fieldset#sigla', [
      m('h3.opcional', [
        'Sigla do serviço',
        m.component(require('tooltips').sigla)
      ]),

      m('div.input-container', {
        class: ctrl.validador.hasError('sigla')
      }, [
        m('input[type=text]', {
          onchange: m.withAttr('value', ctrl.servico().sigla),
          value: ctrl.servico().sigla()
        })
      ])
    ]);
  }
};
