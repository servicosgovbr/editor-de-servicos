'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    return m('fieldset#sigla', [
      m('h3', 'Sigla do serviço'),
      m('input[type=text]', {
        onchange: m.withAttr('value', ctrl.servico().sigla),
        value: ctrl.servico().sigla()
      })
    ]);
  }

};
