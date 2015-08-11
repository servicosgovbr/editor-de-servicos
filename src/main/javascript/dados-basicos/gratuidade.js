'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    return m('fieldset#gratuidade', [
      m('h3', [
        'Gratuidade deste serviço',
        m.component(require('tooltips').gratuidade)
      ]),

      m('label',
        m('input[type=radio][name=gratuidade]', {
          onchange: function () {
            ctrl.servico().gratuidade(true);
          },
          checked: ctrl.servico().gratuidade() !== undefined && ctrl.servico().gratuidade()
        }),
        'Este serviço é gratuito para o solicitante'
      ),

      m('label',
        m('input[type=radio][name=gratuidade]', {
          onchange: function () {
            ctrl.servico().gratuidade(false);
          },
          checked: ctrl.servico().gratuidade() !== undefined && !ctrl.servico().gratuidade()
        }),
        'Este serviço tem custos para o solicitante'
      )


    ]);
  }

};
