'use strict';

module.exports = {

  controller: function (args) {
    this.gratuidade = args.gratuidade;
  },

  view: function (ctrl) {
    return m('fieldset#gratuidade', [
      m('h3', 'Gratuidade'),

      m('label',
        m('input[type=checkbox]', {
          onchange: m.withAttr('checked', ctrl.gratuidade),
          checked: ctrl.gratuidade()
        }),
        'Este serviço é gratuito para o solicitante'
      )
    ]);
  }

};
