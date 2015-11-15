'use strict';

var Gratuidade = require('servico/modelos').Gratuidade;

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    return m('fieldset#gratuidade', [
      m('h3.input-container', {
        class: ctrl.servico().gratuidade.erro()
      }, [
        'Gratuidade deste serviço',
        m.component(require('tooltips').gratuidade)
      ]),

      m('label.input-container',
        m('input[type=radio][name=gratuidade]', {
          onchange: function () {
            ctrl.servico().gratuidade(Gratuidade.GRATUITO);
          },
          checked: ctrl.servico().gratuidade() === Gratuidade.GRATUITO
        }),
        'Este serviço é gratuito para o solicitante'
      ),

      m('label',
        m('input[type=radio][name=gratuidade]', {
          onchange: function () {
            ctrl.servico().gratuidade(Gratuidade.PAGO);
          },
          checked: ctrl.servico().gratuidade() === Gratuidade.PAGO
        }),
        'Este serviço tem custos para o solicitante'
      )
    ]);
  }
};
