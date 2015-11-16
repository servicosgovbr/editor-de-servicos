'use strict';

var carregarServico = require('xml/carregar');

function carregar(cabecalho) {
  return carregarServico(m.route.param('id'), cabecalho);
}

var servicoMantido = m.prop(null);

module.exports = {
  recuperar: function (cabecalho) {
    if (!servicoMantido()) {
      carregar(cabecalho)
        .then(servicoMantido);
    }
    return servicoMantido;
  },

  manter: function (servico) {
    servicoMantido = servico;
  }
};
