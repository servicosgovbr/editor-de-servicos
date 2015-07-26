'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    return m('fieldset#nome', [
      m('h3', 'Nome do serviço'),
      m('input[type=text]', {
        onchange: m.withAttr('value', ctrl.servico().nome),
        value: ctrl.servico().nome()
      })
    ]);
  }

};
