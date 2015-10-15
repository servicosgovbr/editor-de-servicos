'use strict';

var carregarServico = require('xml/carregar');

module.exports = {

  servico: m.prop(null),

  recuperar: function(cabecalho) {
      return _.isNull(this.servico()) ? carregarServico(m.route.param('id'), cabecalho) : this.servico();
  },

  manter: function(servico) {
      this.servico(servico);
  }

};
