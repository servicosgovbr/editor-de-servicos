'use strict';

module.exports = {

  view: function () {
    return m('form#logout[action=/editar/logout][method=POST]', [
      m('button', {
          title: 'Sair do editor (logout)'
        }, m.trust('&nbsp; Sair &nbsp;'),
        m('i.fa.fa-sign-out'))
    ]);
  }

};
