'use strict';

module.exports = {

  view: function (ctrl, args) {
    function metadados() {
      if (!args.metadados) {
        return m('');
      }

      return m.component(require('cabecalho/metadados'), args);
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
