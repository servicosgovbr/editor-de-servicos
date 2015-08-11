'use strict';

module.exports = {

  controller: function (args) {
    var config = _.merge({
      salvar: _.noop,
      cabecalho: {}
    }, args);

    this.salvar = config.salvar;
    this.cabecalho = config.cabecalho;
  },

  view: function (ctrl) {
    return m('header', [
      m('', m('a[href=/editar]', m('h1', 'Editor de Serviços'))),

      m.component(require('cabecalho/metadados'), {
        salvar: _.bind(ctrl.salvar, ctrl)
      }),

      m.component(require('cabecalho/logout'))
    ]);
  }

};
