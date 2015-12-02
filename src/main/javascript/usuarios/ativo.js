'use strict';

module.exports = function (m, valoresAtivo, element) {
  var ativo = {};
  ativo.id = m.prop(true);

  ativo.view = function () {
    return m('label', [
        m.component(require('componentes/select2'), {
        prop: ativo.id,
        tte: {},
        data: valoresAtivo
      }),
        m('input[type=hidden]#habilitado', {
        name: 'habilitado',
        value: ativo.id()
      })
    ]);
  };

  m.mount(document.getElementById(element), {
    view: ativo.view
  });
};
