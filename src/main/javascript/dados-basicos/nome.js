'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
    this.validador = this.servico().validador;
  },

  view: function (ctrl) {
    return m('fieldset#nome', [
      m('h2', 'dados básicos'),
      m('h3', [
        'Nome do serviço',
        m.component(require('tooltips').nome)
      ]),

      m('div.input-container', {
        class: ctrl.validador.hasError('nome')
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
