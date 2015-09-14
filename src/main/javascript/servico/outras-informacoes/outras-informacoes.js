'use strict';

module.exports = {

  controller: function (args) {
    this.servico = args.servico;
  },

  view: function (ctrl) {
    var binding = {
      servico: ctrl.servico
    };

    return m('#outras-informacoes', [
      m.component(require('servico/outras-informacoes/orgao-responsavel'), binding),
      m.component(require('servico/outras-informacoes/segmentos-da-sociedade'), binding),
      m.component(require('servico/outras-informacoes/areas-de-interesse'), binding),
      m.component(require('servico/outras-informacoes/palavras-chave'), binding),
      m.component(require('servico/outras-informacoes/legislacoes'), binding)
    ]);
  }
};
