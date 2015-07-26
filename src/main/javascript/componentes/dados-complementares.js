'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    var binding = {
      servico: ctrl.servico
    };

    return m('#dados-complementares', [
      m.component(require('componentes/orgao-responsavel'), binding),
      m.component(require('componentes/segmentos-da-sociedade'), binding),
      m.component(require('componentes/eventos-da-linha-da-vida'), binding),
      m.component(require('componentes/areas-de-interesse'), binding),
      m.component(require('componentes/palavras-chave'), binding),
      m.component(require('componentes/legislacoes'), binding)
    ]);
  }
};
