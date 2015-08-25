'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
    this.todosOrgaos = require('referencia').orgaos;
  },

  view: function (ctrl) {
    return m('fieldset#orgao-responsavel', [
      m('h2', 'outras informações'),
      m('h3', [
        'Órgão responsável',
        m.component(require('tooltips').orgaoResponsavel)
      ]),

      m('.input-container', [
        m.component(require('componentes/select2'), {
          prop: ctrl.servico().orgao,
          data: ctrl.todosOrgaos(),
          width: '100%',
          minimumResultsForSearch: 1
        })
      ])
    ]);
  }
};
