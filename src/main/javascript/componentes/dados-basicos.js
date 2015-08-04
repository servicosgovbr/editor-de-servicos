'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    var binding = {
      servico: ctrl.servico
    };

    return m('#dados-basicos', [
      m.component(require('dados-basicos/nome'), binding),
      m.component(require('dados-basicos/sigla'), binding),
      m.component(require('dados-basicos/nomes-populares'), binding),
      m.component(require('dados-basicos/descricao'), binding),
      m.component(require('dados-basicos/tempo-total-estimado'), binding),
      m.component(require('dados-basicos/gratuidade'), binding),
    ]);
  }
};
