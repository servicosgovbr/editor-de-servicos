'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    var validador = ctrl.servico().validador;

    return m('fieldset#nome', [
      m('h2', 'dados básicos'),
      m('h3', [
        'Nome do serviço',
        m.component(require('tooltips').nome)
      ]),

      m('div.input-container', {
        class: validador.hasError('nome')
      }, [
        m('input[type=text]', {
          onchange: m.withAttr('value', ctrl.servico().nome),
          value: ctrl.servico().nome(),
          autofocus: 'autofocus'
        })
      ])
    ]);
  }
};
