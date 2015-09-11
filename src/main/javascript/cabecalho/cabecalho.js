'use strict';

module.exports = {

  controller: function (args) {

    var config = _.merge({
      salvar: _.noop,
      publicar: _.noop,
      cabecalho: {}
    }, args);

    this.salvar = config.salvar;
    this.publicar = config.publicar;
    this.cabecalho = config.cabecalho;
  },

  view: function (ctrl, args) {

    function metadados() {
      if (!args.metadados) {
        return m('');
      }
      return m.component(require('cabecalho/metadados'), {
        salvar: _.bind(ctrl.salvar, ctrl),
        publicar: _.bind(ctrl.publicar, ctrl)
      });
    }

    function logout() {
      if (!args.logout) {
        return m('');
      }
      return m.component(require('cabecalho/logout'));
    }

    return m('header', [
      m('', m('a[href=/editar]', m('h1', 'Editor de Serviços'))),

      metadados(),
      logout()
    ]);
  }

};
