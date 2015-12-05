'use strict';

module.exports = function (m, valorDeHabilitado, element) {
  var ativo = {};
  ativo.id = m.prop(valorDeHabilitado.toString());

  ativo.view = function () {
    return m('label', [
        m.component(require('componentes/select2'), {
        prop: ativo.id,
        tte: {},
        data: [{
          id: true,
          text: 'Ativo'
        }, {
          id: false,
          text: 'Inativo'
        }]
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
