'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    var validador = ctrl.servico().validador;

    return m('fieldset#sigla', [
      m('h3.opcional', [
        'Sigla do serviço',
        m.component(require('tooltips').sigla)
      ]),

      m('div.input-container', {
        class: validador.hasError('sigla')
      }, [
        m('input[type=text]', {
          onchange: m.withAttr('value', ctrl.servico().sigla),
          value: ctrl.servico().sigla()
        })
      ])
    ]);
  }
};
