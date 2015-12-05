'use strict';

var selectOrgao = require('pagina/orgao/select-orgao');

module.exports = function (m, siorgInicial, element) {
  var campoSiorg = document.getElementById(element);
  if(!campoSiorg) {
    return;
  }

  var siorg = {};
  siorg.nome = m.prop(siorgInicial);
  siorg.view = function () {
    return m('label', [
        m('h3', 'Orgão'),
        m.component(selectOrgao, {
        prop: siorg.nome
      }),
        m('input[type=hidden]#siorg', {
        name: 'siorg',
        value: siorg.nome()
      })
    ]);
  };

  m.mount(campoSiorg, {
    view: siorg.view
  });
};
