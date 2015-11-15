'use strict';
var protecaoCsrf = require('utils/protecao-csrf');

module.exports = {

  view: function () {
    return m('form#logout[action=/editar/sair][method=POST]', {config: protecaoCsrf}, [
      m('button', {
          title: 'Sair do editor (logout)'
        }, m.trust('&nbsp; Sair &nbsp;'),
        m('i.fa.fa-sign-out'))
    ]);
  }

};
