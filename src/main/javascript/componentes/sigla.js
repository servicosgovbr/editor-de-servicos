'use strict';

module.exports = {

  controller: function (args) {
    this.sigla = args.sigla;
  },

  view: function (ctrl) {
    return m('fieldset#sigla', [
      m('h3', 'Sigla do serviço'),
      m('input[type=text]', {
        onchange: m.withAttr('value', ctrl.sigla),
        value: ctrl.sigla()
      })
    ]);
  }

};
