'use strict';

var tooltips = require('editor-de-paginas/tooltips');

module.exports = {
  view: function (ctrl, args) {
    var pagina = args.pagina();

    var componenteNome = args.nome ?
      m.component(require('orgao/select-orgao'), {
        orgao: pagina.nome
      }) : pagina.nome();

    return m('fieldset#nome', [
      m('h3', [
          'Nome da Página',
          m.component(tooltips.nome)
      ]),
      m('.input-container', componenteNome)
    ]);
  }
};
