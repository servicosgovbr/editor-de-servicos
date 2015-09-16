'use strict';

var tooltips = require('editor-de-paginas/tooltips');

module.exports = {
  view: function (ctrl, args) {
    var pagina = args.pagina();

    return m('fieldset#nome', [
      m('h3', [
          'Nome da Página',
          m.component(tooltips.nome)
      ]),
      m('.input-container', [
        m.component(require('orgao/select-orgao'), {
          orgao: pagina.nome
        })
      ])
    ]);
  }
};
