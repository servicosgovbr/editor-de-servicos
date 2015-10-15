'use strict';

var carregarServico = require('xml/carregar');

module.exports = {

  servico: m.prop(null),

  recuperarServico: function(cabecalho) {
      return _.isNull(this.servico()) ? carregarServico(m.route.param('id'), cabecalho) : this.servico();
  },

  salvarServico: function(servico) {
      this.servico(servico);
  }

};
