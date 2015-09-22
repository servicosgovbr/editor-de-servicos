'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    return m('fieldset#orgao-responsavel', [
      m('h2', 'outras informações'),
      m('h3', [
        'Órgão responsável',
        m.component(require('tooltips').orgaoResponsavel)
      ]),

      m('.input-container', [
        m.component(require('orgao/select-orgao'), {
          prop: ctrl.servico().orgao
        })
      ])
    ]);
  }
};
