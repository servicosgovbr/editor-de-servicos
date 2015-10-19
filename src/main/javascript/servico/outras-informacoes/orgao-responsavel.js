'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    return m('', [
      m('fieldset#orgao-responsavel', [
        m('h2', 'outras informações'),
        m('h3', [
          'Órgão responsável',
          m.component(require('tooltips').orgaoResponsavel)
        ]),

        m('.input-container', [
          m.component(require('orgao/select-orgao'), {
            prop: ctrl.servico().orgao().nome
          })
        ])
      ]),

      m('fieldset#contato-orgao', [
        m('h3.opcional', [
          'Contato do serviço',
          m.component(require('tooltips').contatoOrgao)
        ]),

        m('div.input-container', {
            class: ctrl.servico().orgao().contato.erro(),
          }, 
          m('input[type=text]', {
            onchange: m.withAttr('value', ctrl.servico().orgao().contato),
            value: ctrl.servico().orgao().contato()
          })
        )
      ])
    ]);
  }
};
