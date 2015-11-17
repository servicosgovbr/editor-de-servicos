'use strict';

var carregarServico = require('xml/carregar');

function carregar(cabecalho) {
  return carregarServico(m.route.param('id'), cabecalho);
}

var servicoMantido = m.prop(null);
var metadadosMantido = m.prop(null);

module.exports = {
  recuperar: function (cabecalho) {
    if (!servicoMantido()) {
      carregar(cabecalho)
        .then(function (ser) {
          servicoMantido(ser);
          metadadosMantido = cabecalho.metadados;
        });
    }
    cabecalho.metadados(metadadosMantido());
    return servicoMantido;
  },

  manter: function (servico, metadados) {
    servicoMantido = servico;
    metadadosMantido = metadados;
  }
};
