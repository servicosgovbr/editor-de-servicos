'use strict';

var carregarServico = require('xml/carregar');

var servicoMantido = m.prop(null);

function carregar(cabecalho) {
  return carregarServico(m.route.param('id'), cabecalho);
}

module.exports = {
  recuperar: function (cabecalho) {
    var servicoRetorno = m.prop(null);
    if (servicoMantido()) {
      servicoRetorno(servicoMantido());
    } else {
      carregar(cabecalho)
        .then(servicoRetorno);
    }
    return servicoRetorno;
  },

  manter: function (servico) {
    servicoMantido(servico);
  }
};
