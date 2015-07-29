'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    return m('fieldset#gratuidade', [
      m('h3', 'Gratuidade deste serviço'),

      m('label',
        m('input[type=checkbox]', {
          onchange: m.withAttr('checked', ctrl.servico().gratuidade),
          checked: ctrl.servico().gratuidade()
        }),
        'Este serviço é gratuito para o solicitante'
      )
    ]);
  }

};
