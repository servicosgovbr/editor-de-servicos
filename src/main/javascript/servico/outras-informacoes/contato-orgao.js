'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    return m('fieldset#contato-orgao', [
      m('h3', [
        'Contato do serviço',
        m.component(require('tooltips').contatoOrgao)
      ]),

      m.component(require('componentes/editor-markdown'), {
        rows: 3,
        onchange: m.withAttr('value', ctrl.servico().contatoOrgao),
        // value: ctrl.servico().descricao(),
        // erro: ctrl.servico().descricao.erro()
      })
    ]);
  }
};
