'use strict';

var selectOrgao = require('pagina/orgao/select-orgao');

module.exports = function (m, siorgInicial, element) {
  var siorg = {};
  siorg.nome = m.prop(siorgInicial);
  siorg.view = function () {
    return m('label', [
            'SIORG',
            m.component(selectOrgao, {
        prop: siorg.nome
      }),
            m('input[type=hidden]#siorg', {
        name: 'siorg',
        value: siorg.nome()
      })
        ]);
  };

  m.mount(document.getElementById(element), {
    view: siorg.view
  });
};
