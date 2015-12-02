/*global loggedUser*/
'use strict';
var atributosCsrf = require('utils/atributos-csrf');

module.exports = {

  view: function () {
    return m('form#logout[action=/editar/sair][method=POST]', [
      m('input', {
        type: 'hidden',
        name: atributosCsrf.name,
        value: atributosCsrf.token
      }), m('span.username', loggedUser),
      m('button', {
          title: 'Sair do editor (logout)'
        },
        m('i.fa.fa-sign-out'))
    ]);
  }

};
