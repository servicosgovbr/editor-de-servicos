'use strict';

var tooltips = require('tooltips');

module.exports = {
  view: function (ctrl, args) {
    var pagina = args.pagina();

    var componenteNome = args.novo ? m.component(require('orgao/select-orgao'), {
      orgao: pagina.nome
    }) : pagina.nome();

    return m('fieldset#nome', [
      m('h3', [
          'Órgão',
          args.novo
          ? m.component(tooltips.escolhaOrgao)
          : ''
      ]),
      m('.input-container', componenteNome)
    ]);
  }
};
