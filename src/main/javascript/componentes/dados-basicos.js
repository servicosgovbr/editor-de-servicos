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
      m.component(require('componentes/nome'), binding),
      m.component(require('componentes/sigla'), binding),
      m.component(require('componentes/nomes-populares'), binding),
      m.component(require('componentes/descricao'), binding),
      m.component(require('componentes/tempo-total-estimado'), binding),
      m.component(require('componentes/gratuidade'), binding),
    ]);
  }
};
